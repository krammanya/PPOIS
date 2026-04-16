import pygame
from pygame.sprite import Group

import controls
import menu
from audio import start_background_music
from effects import (
    add_player_hit_effect,
    apply_bomb_effect,
    apply_diamond_effect,
    apply_star_effect,
    draw_animation_effects,
    draw_player_hit_effect,
)
from end_screen import show_end_screen
from loaders import (
    load_and_scale_image,
    load_effect_animation_frames,
    load_enemy_types,
    load_waves,
    load_weapon_configs,
)
from player import Player
from ui import create_wave_banner, draw_health_ui, draw_wave_banner, load_ui_heart_frames
from waves import create_wave


SCREEN_SIZE = (1200, 800)


def run():
    pygame.init()
    screen = pygame.display.set_mode(SCREEN_SIZE)
    pygame.display.set_caption("Crimsoland")
    clock = pygame.time.Clock()
    wave_font = pygame.font.SysFont(None, 52)
    wave_subtitle_font = pygame.font.SysFont(None, 24)
    score_font = pygame.font.SysFont(None, 56)

    bg_image = pygame.image.load("images/field.jpg").convert()
    bg_image = pygame.transform.scale(bg_image, SCREEN_SIZE)
    game_over_image = pygame.transform.smoothscale(
        pygame.image.load("images/game_over.jpg").convert(),
        SCREEN_SIZE
    )
    win_image = pygame.transform.smoothscale(
        pygame.image.load("images/win.jpg").convert(),
        SCREEN_SIZE
    )
    start_button_image = load_and_scale_image("images/buttom_start.png", (260, 78))
    exit_button_image = load_and_scale_image("images/exit.png", (220, 88))

    weapon_configs = load_weapon_configs()
    player = Player(screen)
    heart_frames = load_ui_heart_frames()
    bomb_explosion_frames = load_effect_animation_frames(weapon_configs["bomb"], "bomb_explosion", [110, 110])
    star_hit_frames = load_effect_animation_frames(weapon_configs["star"], "star_hit", [90, 90])
    diamond_hit_frames = load_effect_animation_frames(weapon_configs["diamond"], "diamond_hit", [100, 100])
    bullets = Group()
    evils = Group()

    score = 0
    hit_effects = []
    animation_effects = []

    waves = load_waves()
    enemy_types = load_enemy_types()
    current_wave_index = 0
    game_result = "lose"
    ui_tick = 0
    wave_banner = create_wave_banner(current_wave_index + 1, len(waves))

    create_wave(screen, evils, waves[current_wave_index], enemy_types)

    while True:
        ui_tick += 1
        controls.events(screen, player, bullets)
        player.update_player()

        for one_evil in evils:
            if one_evil.health > 0:
                one_evil.update(player)

        for one_evil in evils:
            if (
                one_evil.health > 0
                and player.rect.colliderect(one_evil.rect)
                and one_evil.can_attack()
            ):
                player.health -= one_evil.damage
                add_player_hit_effect(hit_effects, player, one_evil)

        controls.update_bullets(bullets, screen.get_width(), screen.get_height())

        for bullet in bullets.copy():
            if not bullet.can_hit():
                continue

            if bullet.weapon_type == "bomb":
                hit_target = False

                for one_evil in evils:
                    if bullet.collides_with_enemy(one_evil):
                        hit_target = True
                        break

                if hit_target:
                    if bullet in bullets:
                        bullets.remove(bullet)

                    score += apply_bomb_effect(bullet, evils, animation_effects, bomb_explosion_frames)
                    continue

            else:
                for one_evil in evils.copy():
                    if bullet.collides_with_enemy(one_evil):
                        if bullet in bullets:
                            bullets.remove(bullet)

                        enemy_died = False

                        if bullet.weapon_type == "star":
                            enemy_died = apply_star_effect(bullet, one_evil, animation_effects, star_hit_frames)
                        elif bullet.weapon_type == "diamond":
                            enemy_died = apply_diamond_effect(bullet, one_evil, animation_effects, diamond_hit_frames)

                        if enemy_died:
                            evils.remove(one_evil)
                            score += 1

                        break

        if len(evils) == 0:
            current_wave_index += 1

            if current_wave_index < len(waves):
                create_wave(screen, evils, waves[current_wave_index], enemy_types)
                wave_banner = create_wave_banner(current_wave_index + 1, len(waves))
            else:
                game_result = "win"
                break

        if player.health <= 0:
            game_result = "lose"
            break

        screen.blit(bg_image, (0, 0))
        player.output()

        for one_evil in evils:
            if one_evil.health > 0:
                one_evil.draw()

        for bullet in bullets.sprites():
            bullet.draw_bullet()

        draw_player_hit_effect(screen, hit_effects)
        draw_animation_effects(screen, animation_effects)
        draw_health_ui(screen, player, heart_frames, ui_tick)
        wave_banner = draw_wave_banner(screen, wave_font, wave_subtitle_font, wave_banner)

        pygame.display.flip()
        clock.tick(60)

    end_screen_image = win_image if game_result == "win" else game_over_image
    return show_end_screen(
        screen,
        clock,
        score_font,
        start_button_image,
        exit_button_image,
        end_screen_image,
        game_result,
        score
    )


def main():
    pygame.init()
    start_background_music()

    if not menu.show_main_menu():
        pygame.quit()
        return

    while run():
        pass


if __name__ == "__main__":
    main()

import math

import pygame


HIT_EFFECT_STYLES = {
    "basic": {"color": (255, 110, 110), "glow_color": (255, 190, 190)},
    "fast": {"color": (255, 170, 90), "glow_color": (255, 220, 160)},
    "tank": {"color": (255, 60, 60), "glow_color": (255, 150, 150)},
    "assassin": {"color": (210, 70, 255), "glow_color": (235, 160, 255)},
    "ranger": {"color": (120, 220, 120), "glow_color": (190, 255, 190)}
}


def add_player_hit_effect(hit_effects, player, enemy):
    damage_scale = max(0.5, enemy.damage)
    style = HIT_EFFECT_STYLES.get(enemy.enemy_type, HIT_EFFECT_STYLES["basic"])
    hit_effects.append(
        {
            "x": player.rect.centerx,
            "y": player.rect.centery,
            "radius": int(24 + damage_scale * 8),
            "alpha": min(255, int(130 + damage_scale * 45)),
            "timer": int(10 + damage_scale * 3),
            "grow_speed": max(4, int(4 + damage_scale * 2)),
            "fade_speed": max(10, int(22 - damage_scale * 3)),
            "color": style["color"],
            "glow_color": style["glow_color"]
        }
    )


def draw_player_hit_effect(screen, hit_effects):
    for effect in hit_effects[:]:
        effect["timer"] -= 1
        effect["radius"] += effect.get("grow_speed", 6)
        effect["alpha"] = max(0, effect["alpha"] - effect.get("fade_speed", 18))

        if effect["timer"] <= 0 or effect["alpha"] <= 0:
            hit_effects.remove(effect)
            continue

        size = effect["radius"] * 2 + 20
        surface = pygame.Surface((size, size), pygame.SRCALPHA)
        center = size // 2
        base_color = effect.get("color", (255, 70, 70))
        glow_color = effect.get("glow_color", (255, 180, 180))
        color = (*base_color, effect["alpha"])

        pygame.draw.circle(surface, color, (center, center), effect["radius"], 5)
        pygame.draw.circle(
            surface,
            (*glow_color, max(0, effect["alpha"] // 2)),
            (center, center),
            max(8, effect["radius"] // 2)
        )

        screen.blit(surface, (effect["x"] - center, effect["y"] - center))


def draw_animation_effects(screen, animation_effects):
    for effect in animation_effects[:]:
        effect["tick"] += 1

        if effect["tick"] >= effect["frame_delay"]:
            effect["tick"] = 0
            effect["frame_index"] += 1

        if effect["frame_index"] >= len(effect["frames"]):
            animation_effects.remove(effect)
            continue

        frame = effect["frames"][effect["frame_index"]]
        frame_rect = frame.get_rect(center=(effect["x"], effect["y"]))
        screen.blit(frame, frame_rect)


def add_animation_effect(animation_effects, x, y, frames, frame_delay):
    animation_effects.append(
        {
            "x": x,
            "y": y,
            "frames": frames,
            "frame_index": 0,
            "frame_delay": max(1, frame_delay),
            "tick": 0
        }
    )


def get_enemy_effect_radius(enemy):
    return min(enemy.rect.width, enemy.rect.height) / 2


def apply_bomb_effect(bullet, evils, animation_effects, explosion_frames):
    config = bullet.weapon_config
    explosion_radius = config.get("explosion_radius", 120)
    knockback = config.get("knockback", 20)

    add_animation_effect(
        animation_effects,
        bullet.rect.centerx,
        bullet.rect.centery,
        explosion_frames,
        config.get("animation_frame_delay", config.get("explosion_frame_delay", 3))
    )

    killed_count = 0

    for one_evil in evils.copy():
        dx = one_evil.rect.centerx - bullet.rect.centerx
        dy = one_evil.rect.centery - bullet.rect.centery
        distance = math.hypot(dx, dy)
        enemy_radius = get_enemy_effect_radius(one_evil)

        if distance <= explosion_radius + enemy_radius:
            one_evil.health -= bullet.damage
            one_evil.apply_knockback(bullet.rect.centerx, bullet.rect.centery, knockback)

            if one_evil.health <= 0:
                evils.remove(one_evil)
                killed_count += 1

    return killed_count


def apply_diamond_effect(bullet, enemy, animation_effects, diamond_frames):
    config = bullet.weapon_config
    slow_factor = config.get("slow_factor", 0.5)
    slow_duration_ms = config.get("slow_duration_ms", 2000)

    add_animation_effect(
        animation_effects,
        enemy.rect.centerx,
        enemy.rect.centery,
        diamond_frames,
        config.get("animation_frame_delay", 3)
    )

    enemy.health -= bullet.damage
    enemy.apply_slow(slow_factor, slow_duration_ms)

    return enemy.health <= 0


def apply_star_effect(bullet, enemy, animation_effects, star_frames):
    config = bullet.weapon_config

    add_animation_effect(
        animation_effects,
        enemy.rect.centerx,
        enemy.rect.centery,
        star_frames,
        config.get("animation_frame_delay", 3)
    )

    enemy.health -= bullet.damage
    return enemy.health <= 0

import sys
import pygame
from bullet import Bullet


def shoot(screen, player, bullets):
    weapon_config = player.weapon_data[player.weapon_type]
    new_bullet = Bullet(screen, player, player.weapon_type, weapon_config)
    bullets.add(new_bullet)


def events(screen, player, bullets):
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()

        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_d:
                player.mright = True
            elif event.key == pygame.K_a:
                player.mleft = True
            elif event.key == pygame.K_w:
                player.mup = True
            elif event.key == pygame.K_s:
                player.mdown = True

            elif event.key == pygame.K_1:
                player.weapon_type = "star"
            elif event.key == pygame.K_2:
                player.weapon_type = "bomb"
            elif event.key == pygame.K_3:
                player.weapon_type = "diamond"

        elif event.type == pygame.KEYUP:
            if event.key == pygame.K_d:
                player.mright = False
            elif event.key == pygame.K_a:
                player.mleft = False
            elif event.key == pygame.K_w:
                player.mup = False
            elif event.key == pygame.K_s:
                player.mdown = False

        elif event.type == pygame.MOUSEBUTTONDOWN:
            if event.button == 1:
                shoot(screen, player, bullets)


def update_bullets(bullets, screen_width, screen_height):
    bullets.update()

    for bullet in bullets.copy():
        if (
            bullet.rect.bottom < 0
            or bullet.rect.top > screen_height
            or bullet.rect.right < 0
            or bullet.rect.left > screen_width
        ):
            bullets.remove(bullet)
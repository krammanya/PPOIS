import json
import pygame


class Player:
    def __init__(self, screen):
        self.screen = screen
        self.image = pygame.image.load('images/player.png').convert_alpha()
        self.image = pygame.transform.scale(self.image, (110, 110))

        self.rect = self.image.get_rect()
        self.screen_rect = screen.get_rect()
        self.rect.centerx = self.screen_rect.centerx
        self.rect.bottom = self.screen_rect.bottom - 20

        self.speed = 5
        self.max_health = 48
        self.health = self.max_health

        self.mright = False
        self.mleft = False
        self.mup = False
        self.mdown = False

        with open('configs/weapon.json', 'r', encoding='utf-8-sig') as file:
            self.weapon_data = json.load(file)

        self.weapon_type = "star"

    def output(self):
        self.screen.blit(self.image, self.rect)

    def update_player(self):
        if self.mright and self.rect.right < self.screen_rect.right:
            self.rect.centerx += self.speed
        if self.mleft and self.rect.left > self.screen_rect.left:
            self.rect.centerx -= self.speed
        if self.mup and self.rect.top > self.screen_rect.top:
            self.rect.centery -= self.speed
        if self.mdown and self.rect.bottom < self.screen_rect.bottom:
            self.rect.centery += self.speed

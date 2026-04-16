import math
import pygame


class Bullet(pygame.sprite.Sprite):
    def __init__(self, screen, player, weapon_type, weapon_config):
        super().__init__()
        self.screen = screen
        self.weapon_type = weapon_type
        self.weapon_config = weapon_config

        self.damage = weapon_config["damage"]
        self.speed = weapon_config["speed"]
        self.effect = weapon_config.get("effect", "direct")
        self.trail_width = weapon_config.get("trail_width", 3)
        self.hit_radius = weapon_config.get("hit_radius")

        self.original_image = pygame.image.load(weapon_config["image"]).convert_alpha()
        self.original_image = pygame.transform.scale(
            self.original_image,
            tuple(weapon_config["size"])
        )

        self.rect = self.original_image.get_rect()

        self.x = float(player.rect.centerx)
        self.y = float(player.rect.centery)

        mouse_x, mouse_y = pygame.mouse.get_pos()
        dx = mouse_x - self.x
        dy = mouse_y - self.y

        distance = math.hypot(dx, dy)
        if distance == 0:
            distance = 1

        self.dx = dx / distance
        self.dy = dy / distance

        start_offset = max(player.rect.width, player.rect.height) // 2 + max(self.rect.width, self.rect.height) // 2 + 22
        self.x += self.dx * start_offset
        self.y += self.dy * start_offset

        angle = -math.degrees(math.atan2(self.dy, self.dx))
        self.image = pygame.transform.rotate(self.original_image, angle)
        self.rect = self.image.get_rect(center=(int(self.x), int(self.y)))

        self.prev_x = self.x
        self.prev_y = self.y

        self.age = 0
        self.min_hit_age = 3

    def update(self):
        self.prev_x = self.x
        self.prev_y = self.y

        self.x += self.dx * self.speed
        self.y += self.dy * self.speed

        self.rect.centerx = int(self.x)
        self.rect.centery = int(self.y)

        self.age += 1

    def can_hit(self):
        return self.age >= self.min_hit_age

    def collides_with_enemy(self, enemy):
        if self.hit_radius is None:
            return self.rect.colliderect(enemy.rect)

        dx = enemy.rect.centerx - self.rect.centerx
        dy = enemy.rect.centery - self.rect.centery
        enemy_radius = min(enemy.rect.width, enemy.rect.height) / 2
        return math.hypot(dx, dy) <= self.hit_radius + enemy_radius

    def draw_bullet(self):
        trail_color = (255, 230, 120)

        if self.weapon_type == "bomb":
            trail_color = (255, 160, 80)
        elif self.weapon_type == "diamond":
            trail_color = (120, 220, 255)

        pygame.draw.line(
            self.screen,
            trail_color,
            (int(self.prev_x), int(self.prev_y)),
            (int(self.x), int(self.y)),
            self.trail_width
        )

        self.screen.blit(self.image, self.rect)

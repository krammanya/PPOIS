import math
import pygame


class Evil(pygame.sprite.Sprite):
    def __init__(self, screen, enemy_type, config):
        super().__init__()
        self.screen = screen
        self.enemy_type = enemy_type

        self.image = pygame.image.load(config["image"]).convert_alpha()
        self.image = pygame.transform.scale(self.image, tuple(config["size"]))

        self.rect = self.image.get_rect()
        self.rect.x = self.rect.width
        self.rect.y = self.rect.height

        self.x = float(self.rect.centerx)
        self.y = float(self.rect.centery)

        self.vx = 0.0
        self.vy = 0.0

        self.base_speed = config["speed"]
        self.health = config["health"]
        self.damage = config["damage"]
        self.stop_distance = config["stop_distance"]

        self.attack_cooldown_ms = config.get("attack_cooldown_ms", 700)
        self.last_attack_time = 0
        self.distance = 9999

        self.strafe_direction = 1 if pygame.time.get_ticks() % 2 == 0 else -1
        self.last_strafe_switch_time = pygame.time.get_ticks()

        self.velocity_smoothing = 0.18
        self.min_speed = 0.35

        self.slow_factor = 1.0
        self.slow_end_time = 0

        self.knockback_vx = 0.0
        self.knockback_vy = 0.0
        self.knockback_friction = 0.86

    def update(self, player):
        current_time = pygame.time.get_ticks()

        if current_time >= self.slow_end_time:
            self.slow_factor = 1.0

        dx = player.rect.centerx - self.x
        dy = player.rect.centery - self.y
        self.distance = math.hypot(dx, dy)

        if self.distance == 0:
            self.distance = 1

        target_vx, target_vy = self._get_target_velocity(dx, dy)

        target_vx *= self.slow_factor
        target_vy *= self.slow_factor

        self.vx += (target_vx - self.vx) * self.velocity_smoothing
        self.vy += (target_vy - self.vy) * self.velocity_smoothing

        current_speed = math.hypot(self.vx, self.vy)
        target_speed = math.hypot(target_vx, target_vy)

        if target_speed > 0 and current_speed < self.min_speed * self.slow_factor:
            self.vx = (target_vx / target_speed) * self.min_speed * self.slow_factor
            self.vy = (target_vy / target_speed) * self.min_speed * self.slow_factor

        self.x += self.vx + self.knockback_vx
        self.y += self.vy + self.knockback_vy

        self.knockback_vx *= self.knockback_friction
        self.knockback_vy *= self.knockback_friction

        if abs(self.knockback_vx) < 0.05:
            self.knockback_vx = 0.0
        if abs(self.knockback_vy) < 0.05:
            self.knockback_vy = 0.0

        self.rect.centerx = int(self.x)
        self.rect.centery = int(self.y)

    def apply_slow(self, factor, duration_ms):
        self.slow_factor = min(self.slow_factor, factor)
        self.slow_end_time = pygame.time.get_ticks() + duration_ms

    def apply_knockback(self, source_x, source_y, force):
        dx = self.x - source_x
        dy = self.y - source_y
        distance = math.hypot(dx, dy)

        if distance == 0:
            distance = 1

        self.knockback_vx += (dx / distance) * force
        self.knockback_vy += (dy / distance) * force

    def _get_target_velocity(self, dx, dy):
        if self.enemy_type == "basic":
            return self._towards_velocity(dx, dy, self.base_speed)

        if self.enemy_type == "fast":
            rush_speed = self.base_speed * (1.35 if self.distance < 240 else 1.0)
            return self._towards_velocity(dx, dy, rush_speed)

        if self.enemy_type == "tank":
            return self._towards_velocity(dx, dy, self.base_speed)

        if self.enemy_type == "assassin":
            if self.distance > self.stop_distance + 20:
                rush_speed = self.base_speed * (1.8 if self.distance < 260 else 1.15)
                return self._towards_velocity(dx, dy, rush_speed)
            return self._strafe_velocity(dx, dy, self.base_speed * 0.85)

        if self.enemy_type == "ranger":
            preferred_distance = self.stop_distance + 70
            if self.distance < self.stop_distance - 25:
                return self._away_velocity(dx, dy, self.base_speed * 1.1)
            if self.distance > preferred_distance + 25:
                return self._towards_velocity(dx, dy, self.base_speed * 0.8)
            return self._strafe_velocity(dx, dy, self.base_speed * 0.6)

        return self._towards_velocity(dx, dy, self.base_speed)

    def _towards_velocity(self, dx, dy, speed):
        if self.distance <= self.stop_distance - 8:
            return 0.0, 0.0
        return speed * dx / self.distance, speed * dy / self.distance

    def _away_velocity(self, dx, dy, speed):
        return -speed * dx / self.distance, -speed * dy / self.distance

    def _strafe_velocity(self, dx, dy, speed):
        current_time = pygame.time.get_ticks()

        if current_time - self.last_strafe_switch_time >= 650:
            self.strafe_direction *= -1
            self.last_strafe_switch_time = current_time

        perpendicular_x = -dy / self.distance
        perpendicular_y = dx / self.distance
        forward_bias = 0.2 if self.distance > self.stop_distance else -0.12

        velocity_x = perpendicular_x * speed * self.strafe_direction + (dx / self.distance) * speed * forward_bias
        velocity_y = perpendicular_y * speed * self.strafe_direction + (dy / self.distance) * speed * forward_bias
        return velocity_x, velocity_y

    def draw(self):
        self.screen.blit(self.image, self.rect)

    def can_attack(self):
        current_time = pygame.time.get_ticks()

        if current_time - self.last_attack_time < self.attack_cooldown_ms:
            return False

        self.last_attack_time = current_time
        return True
import math
from pathlib import Path

import pygame


def load_ui_heart_frames():
    heart_dir = Path("images/ui_hearts")
    frames = []

    if heart_dir.exists():
        for pattern in ("*.png", "*.webp", "*.jpg"):
            for frame_path in sorted(heart_dir.glob(pattern)):
                try:
                    frame = pygame.image.load(str(frame_path)).convert()
                except pygame.error:
                    continue

                background_color = frame.get_at((0, 0))
                frame.set_colorkey(background_color)

                surface = pygame.Surface(frame.get_size(), pygame.SRCALPHA)
                surface.blit(frame, (0, 0))
                bounds = surface.get_bounding_rect()

                if bounds.width > 0 and bounds.height > 0:
                    surface = surface.subsurface(bounds).copy()

                surface = pygame.transform.scale(surface, (54, 54))
                frames.append(surface)

    return frames


def draw_health_ui(screen, player, heart_frames, ui_tick):
    if not heart_frames:
        return

    total_hearts = 3
    hp_per_heart = player.max_health / total_hearts if total_hearts else player.max_health

    for index in range(total_hearts):
        heart_health = max(0, min(hp_per_heart, player.health - index * hp_per_heart))
        fill_ratio = 0 if hp_per_heart <= 0 else heart_health / hp_per_heart
        frame_index = min(len(heart_frames) - 1, int((1 - fill_ratio) * (len(heart_frames) - 1)))
        heart_image = heart_frames[frame_index]

        if index == 0 and player.health <= hp_per_heart:
            pulse = 1.0 + 0.08 * math.sin(ui_tick / 6)
            pulse_size = max(28, int(heart_image.get_width() * pulse))
            heart_image = pygame.transform.scale(heart_image, (pulse_size, pulse_size))

        heart_rect = heart_image.get_rect(topleft=(26 + index * 52, 26))
        screen.blit(heart_image, heart_rect)


def create_wave_banner(current_wave_number, total_waves):
    is_final_wave = current_wave_number == total_waves

    if not is_final_wave:
        return None

    return {
        "title": "FINAL WAVE",
        "subtitle": "Hold your ground",
        "timer": 150,
        "alpha": 0,
        "max_alpha": 240,
        "color": (255, 90, 90),
        "subtitle_color": (255, 210, 210)
    }


def draw_wave_banner(screen, title_font, subtitle_font, wave_banner):
    if not wave_banner:
        return None

    wave_banner["timer"] -= 1

    if wave_banner["timer"] > 110:
        wave_banner["alpha"] = min(wave_banner["max_alpha"], wave_banner["alpha"] + 16)
    elif wave_banner["timer"] < 35:
        wave_banner["alpha"] = max(0, wave_banner["alpha"] - 12)
    else:
        wave_banner["alpha"] = wave_banner["max_alpha"]

    if wave_banner["timer"] <= 0 or wave_banner["alpha"] <= 0:
        return None

    overlay = pygame.Surface(screen.get_size(), pygame.SRCALPHA)
    glow_surface = pygame.Surface((460, 112), pygame.SRCALPHA)
    glow_color = (*wave_banner["color"], min(120, wave_banner["alpha"] // 3))
    pygame.draw.rect(glow_surface, glow_color, glow_surface.get_rect(), border_radius=28)
    glow_rect = glow_surface.get_rect(center=(screen.get_width() // 2, 88))
    overlay.blit(glow_surface, glow_rect)

    title_surface = title_font.render(wave_banner["title"], True, wave_banner["color"])
    title_surface.set_alpha(wave_banner["alpha"])
    title_rect = title_surface.get_rect(center=(screen.get_width() // 2, 74))
    overlay.blit(title_surface, title_rect)

    subtitle_surface = subtitle_font.render(wave_banner["subtitle"], True, wave_banner["subtitle_color"])
    subtitle_surface.set_alpha(min(255, wave_banner["alpha"]))
    subtitle_rect = subtitle_surface.get_rect(center=(screen.get_width() // 2, 104))
    overlay.blit(subtitle_surface, subtitle_rect)

    screen.blit(overlay, (0, 0))
    return wave_banner

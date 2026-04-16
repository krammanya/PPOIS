import json
from pathlib import Path

import pygame


def load_waves():
    with open("configs/waves.json", "r", encoding="utf-8-sig") as file:
        data = json.load(file)
    return data["waves"]


def load_enemy_types():
    with open("configs/enemies.json", "r", encoding="utf-8-sig") as file:
        return json.load(file)


def load_weapon_configs():
    with open("configs/weapon.json", "r", encoding="utf-8-sig") as file:
        return json.load(file)


def load_and_scale_image(path, size):
    image = pygame.image.load(path).convert_alpha()
    return pygame.transform.smoothscale(image, size)


def load_effect_animation_frames(config, folder_name, fallback_size):
    frame_size = tuple(config.get("animation_size", config.get("explosion_animation_size", fallback_size)))
    frame_paths = config.get("animation_frames", config.get("explosion_frames", []))
    frames = []

    if frame_paths:
        paths_to_load = [Path(path) for path in frame_paths]
    else:
        effect_dir = Path("images") / folder_name
        paths_to_load = []

        if effect_dir.exists():
            for pattern in ("*.png", "*.webp", "*.jpg"):
                paths_to_load.extend(sorted(effect_dir.glob(pattern)))

    for frame_path in paths_to_load:
        try:
            raw_frame = pygame.image.load(str(frame_path)).convert()
        except pygame.error:
            continue

        background_color = raw_frame.get_at((0, 0))
        raw_frame.set_colorkey(background_color)

        frame = pygame.Surface(raw_frame.get_size(), pygame.SRCALPHA)
        frame.blit(raw_frame, (0, 0))

        bounds = frame.get_bounding_rect()
        if bounds.width > 0 and bounds.height > 0:
            frame = frame.subsurface(bounds).copy()

        scale = min(frame_size[0] / frame.get_width(), frame_size[1] / frame.get_height())
        scaled_size = (
            max(1, int(frame.get_width() * scale)),
            max(1, int(frame.get_height() * scale))
        )
        frame = pygame.transform.smoothscale(frame, scaled_size)

        canvas = pygame.Surface(frame_size, pygame.SRCALPHA)
        frame_rect = frame.get_rect(center=(frame_size[0] // 2, frame_size[1] // 2))
        canvas.blit(frame, frame_rect)
        frames.append(canvas)

    if frames:
        return frames

    fallback_path = Path(config.get("fallback_animation_image", config.get("fallback_explosion_image", "images/explosion.jpg")))
    fallback_frame = pygame.image.load(str(fallback_path)).convert_alpha()
    return [pygame.transform.scale(fallback_frame, frame_size)]

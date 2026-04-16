from pathlib import Path

import pygame


BACKGROUND_MUSIC_PATH = Path("sounds/background_music.mp3")
BACKGROUND_MUSIC_VOLUME = 0.35


def start_background_music():
    if not BACKGROUND_MUSIC_PATH.exists():
        return

    try:
        if not pygame.mixer.get_init():
            pygame.mixer.init()

        pygame.mixer.music.load(str(BACKGROUND_MUSIC_PATH))
        pygame.mixer.music.set_volume(BACKGROUND_MUSIC_VOLUME)
        pygame.mixer.music.play(-1)
    except pygame.error as error:
        print(f"Could not play background music: {error}")

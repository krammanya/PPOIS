import json
from pathlib import Path

import pygame


SCREEN_SIZE = (1200, 800)
SETTINGS_PATH = Path("configs/settings.json")


def load_best_score():
    try:
        with SETTINGS_PATH.open("r", encoding="utf-8-sig") as file:
            data = json.load(file)
    except (FileNotFoundError, json.JSONDecodeError):
        return 0

    return int(data.get("best_score", 0))


def save_best_score(score):
    best_score = max(load_best_score(), int(score))

    with SETTINGS_PATH.open("w", encoding="utf-8") as file:
        json.dump({"best_score": best_score}, file, ensure_ascii=False, indent=2)

    return best_score


def load_and_scale_image(path, size, alpha=True):
    image = pygame.image.load(path)
    image = image.convert_alpha() if alpha else image.convert()
    return pygame.transform.smoothscale(image, size)


def try_load_and_scale_image(path, size, alpha=True):
    file_path = Path(path)
    if not file_path.exists():
        return None

    return load_and_scale_image(str(file_path), size, alpha)


def draw_button(screen, image, rect, mouse_pos):
    draw_rect = rect.copy()

    if rect.collidepoint(mouse_pos):
        draw_rect.y -= 4

    screen.blit(image, draw_rect)

def show_main_menu():
    screen = pygame.display.set_mode(SCREEN_SIZE)
    pygame.display.set_caption("Crimsoland")
    clock = pygame.time.Clock()

    background_image = load_and_scale_image("images/background.jpg", SCREEN_SIZE, alpha=False)
    title_image = try_load_and_scale_image("images/menu.png", (360, 170))
    rules_image = load_and_scale_image("images/rules.png", (470, 470))
    start_button_image = load_and_scale_image("images/buttom_start.png", (260, 78))
    exit_button_image = load_and_scale_image("images/exit.png", (220, 88))

    title_rect = title_image.get_rect(center=(600, 170)) if title_image else None
    rules_rect = rules_image.get_rect(center=(285, 430))
    start_rect = start_button_image.get_rect(center=(910, 430))
    exit_rect = exit_button_image.get_rect(center=(910, 555))

    while True:
        mouse_pos = pygame.mouse.get_pos()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return False
            if event.type == pygame.KEYDOWN and event.key == pygame.K_ESCAPE:
                return False
            if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
                if start_rect.collidepoint(event.pos):
                    return True
                if exit_rect.collidepoint(event.pos):
                    return False

        screen.blit(background_image, (0, 0))

        if title_image and title_rect:
            screen.blit(title_image, title_rect)
        screen.blit(rules_image, rules_rect)

        draw_button(screen, start_button_image, start_rect, mouse_pos)
        draw_button(screen, exit_button_image, exit_rect, mouse_pos)

        pygame.display.flip()
        clock.tick(60)

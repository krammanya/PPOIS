import pygame

import menu


def show_end_screen(screen, clock, score_font, start_button_image, exit_button_image, end_screen_image, game_result, score):
    menu.save_best_score(score)

    start_button_rect = start_button_image.get_rect(center=(600, 545))
    exit_button_rect = exit_button_image.get_rect(center=(600, 650))
    score_text = score_font.render(f"Score: {score}", True, (255, 244, 205))
    score_shadow = score_font.render(f"Score: {score}", True, (35, 12, 12))
    score_panel = pygame.Surface((340, 92), pygame.SRCALPHA)
    pygame.draw.rect(score_panel, (22, 10, 10, 165), score_panel.get_rect(), border_radius=22)
    pygame.draw.rect(score_panel, (255, 220, 160, 120), score_panel.get_rect(), 3, border_radius=22)

    while True:
        mouse_pos = pygame.mouse.get_pos()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                return False
            if event.type == pygame.KEYDOWN and event.key == pygame.K_ESCAPE:
                pygame.quit()
                return False
            if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
                if start_button_rect.collidepoint(event.pos):
                    return True
                if exit_button_rect.collidepoint(event.pos):
                    pygame.quit()
                    return False

        screen.blit(end_screen_image, (0, 0))

        if game_result != "win":
            score_panel_rect = score_panel.get_rect(center=(600, 408))
            screen.blit(score_panel, score_panel_rect)
            screen.blit(score_shadow, score_shadow.get_rect(center=(603, 410)))
            screen.blit(score_text, score_text.get_rect(center=(600, 406)))

        start_draw_rect = start_button_rect.copy()
        exit_draw_rect = exit_button_rect.copy()

        if start_button_rect.collidepoint(mouse_pos):
            start_draw_rect.y -= 4
        if exit_button_rect.collidepoint(mouse_pos):
            exit_draw_rect.y -= 4

        screen.blit(start_button_image, start_draw_rect)
        screen.blit(exit_button_image, exit_draw_rect)
        pygame.display.flip()
        clock.tick(60)

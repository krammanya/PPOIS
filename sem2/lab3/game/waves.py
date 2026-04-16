from random import randint

from evil import Evil


def spawn_enemy(screen, evils_group, enemy_type, enemy_types):
    enemy_config = enemy_types[enemy_type]
    new_evil = Evil(screen, enemy_type, enemy_config)

    side = randint(1, 4)

    if side == 1:
        new_evil.rect.x = randint(0, screen.get_width() - new_evil.rect.width)
        new_evil.rect.y = -new_evil.rect.height
    elif side == 2:
        new_evil.rect.x = randint(0, screen.get_width() - new_evil.rect.width)
        new_evil.rect.y = screen.get_height()
    elif side == 3:
        new_evil.rect.x = -new_evil.rect.width
        new_evil.rect.y = randint(0, screen.get_height() - new_evil.rect.height)
    else:
        new_evil.rect.x = screen.get_width()
        new_evil.rect.y = randint(0, screen.get_height() - new_evil.rect.height)

    new_evil.x = float(new_evil.rect.centerx)
    new_evil.y = float(new_evil.rect.centery)
    evils_group.add(new_evil)


def create_wave(screen, evils_group, wave_config, enemy_types):
    if "enemies" in wave_config:
        for enemy_entry in wave_config["enemies"]:
            for _ in range(enemy_entry["count"]):
                spawn_enemy(screen, evils_group, enemy_entry["type"], enemy_types)
        return

    for _ in range(wave_config["count"]):
        spawn_enemy(screen, evils_group, wave_config["enemy_type"], enemy_types)

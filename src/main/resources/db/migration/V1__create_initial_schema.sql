CREATE TABLE crop(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price_normal INTEGER NOT NULL,
    price_silver INTEGER,
    price_gold INTEGER,
    price_iridium INTEGER,
    recurring BOOLEAN DEFAULT FALSE,
    growth_time INTEGER NOT NULL,
    regrowth_time INTEGER,
    season VARCHAR(20) NOT NULL
);

CREATE TABLE sprinkler(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    radius INTEGER NOT NULL
);

CREATE TABLE player_state(
    id BIGSERIAL PRIMARY KEY,
    farming_level INTEGER DEFAULT 0,
    current_gold INTEGER DEFAULT 0,
    current_day INTEGER DEFAULT 1,
    current_season VARCHAR(50),
    profession VARCHAR(50)
);

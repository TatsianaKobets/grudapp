CREATE TABLE IF NOT EXISTS users (
                                     id INT PRIMARY KEY,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TABLE IF NOT EXISTS habits (
                                      id INT PRIMARY KEY,
                                      user_id INT NOT NULL,
                                      name VARCHAR(255) NOT NULL,
                                      description TEXT,
                                      frequency VARCHAR(255) NOT NULL CHECK(frequency IN ('daily', 'weekly')),
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_habits()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger_habits
    BEFORE UPDATE ON habits
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_habits();

CREATE TABLE IF NOT EXISTS habit_logs (
                                          id INT PRIMARY KEY,
                                          habit_id INT NOT NULL,
                                          user_id INT NOT NULL,
                                          log_date DATE NOT NULL,
                                          completed BOOLEAN NOT NULL DEFAULT FALSE,
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_habit_logs()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger_habit_logs
    BEFORE UPDATE ON habit_logs
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_habit_logs();

CREATE TABLE IF NOT EXISTS habit_stats (
                                           id INT PRIMARY KEY,
                                           habit_id INT NOT NULL,
                                           user_id INT NOT NULL,
                                           streak INT NOT NULL DEFAULT 0,
                                           success_rate FLOAT NOT NULL DEFAULT 0,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_habit_stats()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger_habit_stats
    BEFORE UPDATE ON habit_stats
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_habit_stats();

CREATE TABLE IF NOT EXISTS admins (
                                      id INT PRIMARY KEY,
                                      user_id INT NOT NULL,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_admins()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger_admins
    BEFORE UPDATE ON admins
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_admins();

CREATE TABLE IF NOT EXISTS notifications (
                                             id INT PRIMARY KEY,
                                             user_id INT NOT NULL,
                                             habit_id INT NOT NULL,
                                             notification_date DATE NOT NULL,
                                             sent BOOLEAN NOT NULL DEFAULT FALSE,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_notifications()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger_notifications
    BEFORE UPDATE ON notifications
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_notifications();
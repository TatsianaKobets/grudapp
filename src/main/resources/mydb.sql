CREATE TABLE IF NOT EXISTS UserEntity
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS UserSite
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    namesite VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES UserEntity(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS SitePage
(
    id SERIAL PRIMARY KEY,
    namepage VARCHAR(255)

);
CREATE TABLE IF NOT EXISTS Advertising
(
     id SERIAL PRIMARY KEY,
    infotext TEXT
);

CREATE TABLE IF NOT EXISTS SitePage_Advertising
(
    sitepage_id INT,
    advertising_id INT,
    PRIMARY KEY (sitepage_id, advertising_id),
    FOREIGN KEY (sitepage_id) REFERENCES SitePage (id) ON DELETE CASCADE,
    FOREIGN KEY (advertising_id) REFERENCES Advertising (id) ON DELETE CASCADE
);
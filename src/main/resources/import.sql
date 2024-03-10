CREATE TABLE IF NOT EXISTS Note (
    id UUID PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    body TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Tag (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Note_Tag (
    note_id UUID,
    tag_id UUID,
    PRIMARY KEY (note_id, tag_id),
    FOREIGN KEY (note_id) REFERENCES Note(id),
    FOREIGN KEY (tag_id) REFERENCES Tag(id)
);
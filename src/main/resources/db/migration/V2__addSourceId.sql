ALTER TABLE vacancy_snapshot
    ADD COLUMN source_id text;

ALTER TABLE vacancy_snapshot
    ADD COLUMN mail_source text;

-- уникальная связка source_id + source
CREATE UNIQUE INDEX idx_vacancy_snapshot_source_id
    ON vacancy_snapshot(source_id, source);

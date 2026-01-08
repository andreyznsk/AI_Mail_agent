create extension if not exists "uuid-ossp";

-- =========================
-- VACANCY SNAPSHOT
-- =========================
create table vacancy_snapshot (
                                  id uuid primary key default uuid_generate_v4(),

                                  company_name text not null,
                                  position_title text not null,
                                  salary bigint,
                                  region text,

                                  vacancy_text text not null,
                                  source text not null,

                                  received_at timestamp with time zone not null,
                                  created_at timestamp with time zone not null default now()
);

create index idx_vacancy_snapshot_company
    on vacancy_snapshot(company_name);

-- =========================
-- MATCH RESULT (DEBUG)
-- =========================
create table match_result_snapshot (
                                       id uuid primary key default uuid_generate_v4(),

                                       vacancy_id uuid not null references vacancy_snapshot(id) on delete cascade,

                                       score integer not null,
                                       strengths jsonb not null,
                                       gaps jsonb not null,
                                       summary text not null,

                                       matcher_version text not null,
                                       validation_passed boolean not null,

                                       created_at timestamp with time zone not null default now()
);

create index idx_match_result_vacancy
    on match_result_snapshot(vacancy_id);

-- =========================
-- COVER LETTER
-- =========================
create table cover_letter_snapshot (
                                       id uuid primary key default uuid_generate_v4(),

                                       vacancy_id uuid not null references vacancy_snapshot(id) on delete cascade,

                                       cover_letter_text text not null,

                                       llm_provider text not null,
                                       prompt_version text not null,

                                       created_at timestamp with time zone not null default now()
);

create index idx_cover_letter_vacancy
    on cover_letter_snapshot(vacancy_id);

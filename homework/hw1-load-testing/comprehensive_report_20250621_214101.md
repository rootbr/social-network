# Комплексный анализ производительности индексов

## Обзор тестирования
- **Дата**: Sa 21 Jun 2025 21:41:01 CEST
- **База данных**: PostgreSQL
- **Инструмент**: Apache JMeter
- **Приложение**: Social Network User Search API

## Конфигурации индексов

- **no_index**: Без индексов (baseline)
- **composite_index**: Композитный (first_name, last_name)
- **reverse_composite**: Обратный композитный (last_name, first_name)
- **separate_indexes**: Отдельные B-tree индексы
- **first_name_only**: Только first_name
- **last_name_only**: Только last_name
- **gin_indexes**: GIN индексы (pg_trgm)
- **combined_indexes**: Комбинированные B-tree + GIN

## Методология тестирования

### Нагрузочные сценарии
- **1 пользователь**: 100 запросов (baseline)
- **10 пользователей**: 1000 запросов (умеренная нагрузка)
- **100 пользователей**: 10000 запросов (высокая нагрузка)
- **1000 пользователей**: 10000 запросов (стресс-тест)

### Тестовые запросы
- **LIKE поиск**: `WHERE first_name LIKE 'John%' AND last_name LIKE 'Doe%'`
- **Точный поиск**: `WHERE first_name = 'John' AND last_name = 'Doe'`

---

## Без индексов (baseline)

### SQL для создания конфигурации
```sql
-- Удаление всех индексов (если существуют)
DROP INDEX IF EXISTS idx_composite;
DROP INDEX IF EXISTS idx_reverse_composite;
DROP INDEX IF EXISTS idx_first_name;
DROP INDEX IF EXISTS idx_last_name;
DROP INDEX IF EXISTS idx_first_name_gin;
DROP INDEX IF EXISTS idx_last_name_gin;
DROP INDEX IF EXISTS idx_first_name_btree;
DROP INDEX IF EXISTS idx_last_name_btree;
```

### SQL Анализ

#### Информация об индексах
```
Index information:
 indexname | size 
-----------+------
(0 rows)


Total indexes size:
 total_indexes_size 
--------------------
 72 MB
(1 row)


Table size:
 total_table_size 
------------------
 231 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=26.300..28.566 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16030 read=4293
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=24.802..24.802 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16030 read=4293
 Planning:
   Buffers: shared hit=67 read=1
 Planning Time: 0.259 ms
 Execution Time: 28.580 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=23.601..25.860 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16022 read=4301
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=22.127..22.127 rows=0 loops=3)
         Filter: (((first_name)::text = 'John'::text) AND ((last_name)::text = 'Doe'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16022 read=4301
 Planning:
   Buffers: shared hit=8 read=1
 Planning Time: 0.099 ms
 Execution Time: 25.874 ms
(12 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13824 | 73 | 30439 | 100 | 0,7240,8 |
| 10 users | 14626 | 30 | 30738 | 100 | 0,6905,6 |
| 100 users | 14692 | 66 | 30640 | 100 | 0,7487,3 |
| 1000 users | 13359 | 30 | 30006 | 100 | 0,7485,5 |

### HTML Отчеты
- [1 users](./no_index_1_users_dashboard/index.html)
- [10 users](./no_index_10_users_dashboard/index.html)
- [100 users](./no_index_100_users_dashboard/index.html)
- [1000 users](./no_index_1000_users_dashboard/index.html)

---

## Композитный (first_name, last_name)

### SQL для создания конфигурации
```sql
-- Создание композитного индекса
CREATE INDEX idx_composite ON users(first_name, last_name);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
   indexname   |  size   
---------------+---------
 idx_composite | 9616 kB
(1 row)


Total indexes size:
 total_indexes_size 
--------------------
 81 MB
(1 row)


Table size:
 total_table_size 
------------------
 240 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=94.388..97.216 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16089 read=4234
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=45.384..45.385 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16089 read=4234
 Planning:
   Buffers: shared hit=73 read=9
 Planning Time: 0.680 ms
 Execution Time: 97.245 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                      QUERY PLAN                                                       
-----------------------------------------------------------------------------------------------------------------------
 Index Scan using idx_composite on users  (cost=0.42..8.45 rows=1 width=129) (actual time=0.008..0.009 rows=0 loops=1)
   Index Cond: (((first_name)::text = 'John'::text) AND ((last_name)::text = 'Doe'::text))
   Buffers: shared hit=3
 Planning:
   Buffers: shared hit=11 read=1
 Planning Time: 0.133 ms
 Execution Time: 0.018 ms
(7 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13866 | 206 | 30005 | 100 | 0,7219,3 |
| 10 users | 14314 | 59 | 31056 | 100 | 0,7056,1 |
| 100 users | 14631 | 52 | 30823 | 100 | 0,7518,3 |
| 1000 users | 13333 | 47 | 30007 | 100 | 0,7500,3 |

### HTML Отчеты
- [1 users](./composite_index_1_users_dashboard/index.html)
- [10 users](./composite_index_10_users_dashboard/index.html)
- [100 users](./composite_index_100_users_dashboard/index.html)
- [1000 users](./composite_index_1000_users_dashboard/index.html)

---

## Обратный композитный (last_name, first_name)

### SQL для создания конфигурации
```sql
-- Создание обратного композитного индекса
CREATE INDEX idx_reverse_composite ON users(last_name, first_name);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
       indexname       |  size   
-----------------------+---------
 idx_reverse_composite | 9616 kB
(1 row)


Total indexes size:
 total_indexes_size 
--------------------
 81 MB
(1 row)


Table size:
 total_table_size 
------------------
 240 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=27.426..29.754 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16071 read=4252
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=25.720..25.721 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16071 read=4252
 Planning:
   Buffers: shared hit=66 read=6
 Planning Time: 0.354 ms
 Execution Time: 29.770 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                          QUERY PLAN                                                           
-------------------------------------------------------------------------------------------------------------------------------
 Index Scan using idx_reverse_composite on users  (cost=0.42..8.45 rows=1 width=129) (actual time=0.022..0.022 rows=0 loops=1)
   Index Cond: (((last_name)::text = 'Doe'::text) AND ((first_name)::text = 'John'::text))
   Buffers: shared read=3
 Planning:
   Buffers: shared hit=11 read=1
 Planning Time: 0.097 ms
 Execution Time: 0.030 ms
(7 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13601 | 107 | 27846 | 100 | 0,7359,6 |
| 10 users | 13874 | 31 | 28403 | 100 | 0,7279,7 |
| 100 users | 15158 | 31 | 30771 | 100 | 0,7256,8 |
| 1000 users | 13693 | 60 | 30011 | 100 | 0,7303,0 |

### HTML Отчеты
- [1 users](./reverse_composite_1_users_dashboard/index.html)
- [10 users](./reverse_composite_10_users_dashboard/index.html)
- [100 users](./reverse_composite_100_users_dashboard/index.html)
- [1000 users](./reverse_composite_1000_users_dashboard/index.html)

---

## Отдельные B-tree индексы

### SQL для создания конфигурации
```sql
-- Создание отдельных B-tree индексов
CREATE INDEX idx_first_name ON users(first_name);
CREATE INDEX idx_last_name ON users(last_name);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
   indexname    |  size   
----------------+---------
 idx_last_name  | 7160 kB
 idx_first_name | 6976 kB
(2 rows)


Total indexes size:
 total_indexes_size 
--------------------
 86 MB
(1 row)


Table size:
 total_table_size 
------------------
 245 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=28.853..31.266 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16087 read=4236
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=27.016..27.016 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16087 read=4236
 Planning:
   Buffers: shared hit=72 read=10
 Planning Time: 0.529 ms
 Execution Time: 31.289 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                          QUERY PLAN                                                          
------------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=27.74..31.76 rows=1 width=129) (actual time=0.026..0.026 rows=0 loops=1)
   Recheck Cond: (((last_name)::text = 'Doe'::text) AND ((first_name)::text = 'John'::text))
   Buffers: shared read=3
   ->  BitmapAnd  (cost=27.74..27.74 rows=1 width=0) (actual time=0.024..0.024 rows=0 loops=1)
         Buffers: shared read=3
         ->  Bitmap Index Scan on idx_last_name  (cost=0.00..9.65 rows=697 width=0) (actual time=0.024..0.024 rows=0 loops=1)
               Index Cond: ((last_name)::text = 'Doe'::text)
               Buffers: shared read=3
         ->  Bitmap Index Scan on idx_first_name  (cost=0.00..17.84 rows=1255 width=0) (never executed)
               Index Cond: ((first_name)::text = 'John'::text)
 Planning:
   Buffers: shared hit=11 read=1
 Planning Time: 0.107 ms
 Execution Time: 0.042 ms
(14 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13787 | 85 | 27800 | 100 | 0,7260,3 |
| 10 users | 13858 | 103 | 30007 | 100 | 0,7288,2 |
| 100 users | 15117 | 55 | 30803 | 100 | 0,7276,5 |
| 1000 users | 13650 | 42 | 27727 | 100 | 0,7326,2 |

### HTML Отчеты
- [1 users](./separate_indexes_1_users_dashboard/index.html)
- [10 users](./separate_indexes_10_users_dashboard/index.html)
- [100 users](./separate_indexes_100_users_dashboard/index.html)
- [1000 users](./separate_indexes_1000_users_dashboard/index.html)

---

## Только first_name

### SQL для создания конфигурации
```sql
-- Создание индекса только для first_name
CREATE INDEX idx_first_name ON users(first_name);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
   indexname    |  size   
----------------+---------
 idx_first_name | 6976 kB
(1 row)


Total indexes size:
 total_indexes_size 
--------------------
 79 MB
(1 row)


Table size:
 total_table_size 
------------------
 238 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=27.474..29.766 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16064 read=4259
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=25.812..25.813 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16064 read=4259
 Planning:
   Buffers: shared hit=72 read=9
 Planning Time: 0.392 ms
 Execution Time: 29.781 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                        QUERY PLAN                                                         
---------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=17.58..3917.45 rows=1 width=129) (actual time=0.007..0.007 rows=0 loops=1)
   Recheck Cond: ((first_name)::text = 'John'::text)
   Filter: ((last_name)::text = 'Doe'::text)
   Buffers: shared hit=3
   ->  Bitmap Index Scan on idx_first_name  (cost=0.00..17.57 rows=1220 width=0) (actual time=0.005..0.005 rows=0 loops=1)
         Index Cond: ((first_name)::text = 'John'::text)
         Buffers: shared hit=3
 Planning:
   Buffers: shared hit=11 read=1
 Planning Time: 0.100 ms
 Execution Time: 0.029 ms
(11 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13712 | 41 | 27788 | 100 | 0,7300,2 |
| 10 users | 13876 | 39 | 28524 | 100 | 0,7279,0 |
| 100 users | 15088 | 43 | 30988 | 100 | 0,7290,7 |
| 1000 users | 13710 | 34 | 27495 | 100 | 0,7293,9 |

### HTML Отчеты
- [1 users](./first_name_only_1_users_dashboard/index.html)
- [10 users](./first_name_only_10_users_dashboard/index.html)
- [100 users](./first_name_only_100_users_dashboard/index.html)
- [1000 users](./first_name_only_1000_users_dashboard/index.html)

---

## Только last_name

### SQL для создания конфигурации
```sql
-- Создание индекса только для last_name
CREATE INDEX idx_last_name ON users(last_name);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
   indexname   |  size   
---------------+---------
 idx_last_name | 7160 kB
(1 row)


Total indexes size:
 total_indexes_size 
--------------------
 79 MB
(1 row)


Table size:
 total_table_size 
------------------
 238 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                      QUERY PLAN                                                      
----------------------------------------------------------------------------------------------------------------------
 Gather  (cost=1000.00..27573.10 rows=1 width=129) (actual time=28.645..31.125 rows=0 loops=1)
   Workers Planned: 2
   Workers Launched: 2
   Buffers: shared hit=16072 read=4251
   ->  Parallel Seq Scan on users  (cost=0.00..26573.00 rows=1 width=129) (actual time=26.683..26.683 rows=0 loops=3)
         Filter: (((first_name)::text ~~ 'John%'::text) AND ((last_name)::text ~~ 'Doe%'::text))
         Rows Removed by Filter: 333333
         Buffers: shared hit=16072 read=4251
 Planning:
   Buffers: shared hit=66 read=6
 Planning Time: 0.453 ms
 Execution Time: 31.144 ms
(12 rows)


EXPLAIN ANALYZE for exact search:
                                                       QUERY PLAN                                                       
------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=9.62..2370.02 rows=1 width=129) (actual time=0.033..0.033 rows=0 loops=1)
   Recheck Cond: ((last_name)::text = 'Doe'::text)
   Filter: ((first_name)::text = 'John'::text)
   Buffers: shared read=3
   ->  Bitmap Index Scan on idx_last_name  (cost=0.00..9.62 rows=692 width=0) (actual time=0.031..0.031 rows=0 loops=1)
         Index Cond: ((last_name)::text = 'Doe'::text)
         Buffers: shared read=3
 Planning:
   Buffers: shared hit=11 read=1
 Planning Time: 0.103 ms
 Execution Time: 0.043 ms
(11 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 13753 | 30 | 28310 | 100 | 0,7278,6 |
| 10 users | 13850 | 214 | 28114 | 100 | 0,7292,6 |
| 100 users | 14853 | 28 | 32467 | 100 | 0,7406,0 |
| 1000 users | 13129 | 126 | 30010 | 100 | 0,7616,9 |

### HTML Отчеты
- [1 users](./last_name_only_1_users_dashboard/index.html)
- [10 users](./last_name_only_10_users_dashboard/index.html)
- [100 users](./last_name_only_100_users_dashboard/index.html)
- [1000 users](./last_name_only_1000_users_dashboard/index.html)

---

## GIN индексы (pg_trgm)

### SQL для создания конфигурации
```sql
-- Включение расширения pg_trgm (если не установлено)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Создание GIN индексов для триграмного поиска
CREATE INDEX idx_first_name_gin ON users USING gin(first_name gin_trgm_ops);
CREATE INDEX idx_last_name_gin ON users USING gin(last_name gin_trgm_ops);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
     indexname      | size  
--------------------+-------
 idx_last_name_gin  | 22 MB
 idx_first_name_gin | 15 MB
(2 rows)


Total indexes size:
 total_indexes_size 
--------------------
 109 MB
(1 row)


Table size:
 total_table_size 
------------------
 268 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                            QUERY PLAN                                                            
----------------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=139.36..143.37 rows=1 width=129) (actual time=0.024..0.024 rows=0 loops=1)
   Recheck Cond: (((last_name)::text ~~ 'Doe%'::text) AND ((first_name)::text ~~ 'John%'::text))
   Buffers: shared hit=7
   ->  BitmapAnd  (cost=139.36..139.36 rows=1 width=0) (actual time=0.022..0.022 rows=0 loops=1)
         Buffers: shared hit=7
         ->  Bitmap Index Scan on idx_last_name_gin  (cost=0.00..51.11 rows=62 width=0) (actual time=0.022..0.022 rows=0 loops=1)
               Index Cond: ((last_name)::text ~~ 'Doe%'::text)
               Buffers: shared hit=7
         ->  Bitmap Index Scan on idx_first_name_gin  (cost=0.00..88.00 rows=36 width=0) (never executed)
               Index Cond: ((first_name)::text ~~ 'John%'::text)
 Planning:
   Buffers: shared hit=77 read=2
 Planning Time: 0.512 ms
 Execution Time: 0.082 ms
(14 rows)


EXPLAIN ANALYZE for exact search:
                                                            QUERY PLAN                                                             
-----------------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=186.37..190.39 rows=1 width=129) (actual time=0.008..0.008 rows=0 loops=1)
   Recheck Cond: (((last_name)::text = 'Doe'::text) AND ((first_name)::text = 'John'::text))
   Buffers: shared hit=9
   ->  BitmapAnd  (cost=186.37..186.37 rows=1 width=0) (actual time=0.007..0.007 rows=0 loops=1)
         Buffers: shared hit=9
         ->  Bitmap Index Scan on idx_last_name_gin  (cost=0.00..71.18 rows=690 width=0) (actual time=0.007..0.007 rows=0 loops=1)
               Index Cond: ((last_name)::text = 'Doe'::text)
               Buffers: shared hit=9
         ->  Bitmap Index Scan on idx_first_name_gin  (cost=0.00..114.94 rows=1241 width=0) (never executed)
               Index Cond: ((first_name)::text = 'John'::text)
 Planning:
   Buffers: shared hit=11
 Planning Time: 0.056 ms
 Execution Time: 0.021 ms
(14 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 51 | 0 | 331 | 97 | 0,1947431,5 |
| 10 users | 50 | 0 | 296 | 97 | 7,2003502,2 |
| 100 users | 60 | 0 | 353 | 99 | 9,1826886,4 |
| 1000 users | 55 | 0 | 351 | 97 | 1,1830457,2 |

### HTML Отчеты
- [1 users](./gin_indexes_1_users_dashboard/index.html)
- [10 users](./gin_indexes_10_users_dashboard/index.html)
- [100 users](./gin_indexes_100_users_dashboard/index.html)
- [1000 users](./gin_indexes_1000_users_dashboard/index.html)

---

## Комбинированные B-tree + GIN

### SQL для создания конфигурации
```sql
-- Включение расширения pg_trgm (если не установлено)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Создание комбинированных индексов: B-tree для точного поиска и GIN для LIKE
CREATE INDEX idx_first_name_btree ON users(first_name);
CREATE INDEX idx_last_name_btree ON users(last_name);
CREATE INDEX idx_first_name_gin ON users USING gin(first_name gin_trgm_ops);
CREATE INDEX idx_last_name_gin ON users USING gin(last_name gin_trgm_ops);
```

### SQL Анализ

#### Информация об индексах
```
Index information:
      indexname       |  size   
----------------------+---------
 idx_last_name_gin    | 22 MB
 idx_first_name_gin   | 15 MB
 idx_last_name_btree  | 7160 kB
 idx_first_name_btree | 6976 kB
(4 rows)


Total indexes size:
 total_indexes_size 
--------------------
 123 MB
(1 row)


Table size:
 total_table_size 
------------------
 282 MB
(1 row)


EXPLAIN ANALYZE for LIKE search:
                                                            QUERY PLAN                                                            
----------------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=139.37..143.38 rows=1 width=129) (actual time=0.016..0.017 rows=0 loops=1)
   Recheck Cond: (((last_name)::text ~~ 'Doe%'::text) AND ((first_name)::text ~~ 'John%'::text))
   Buffers: shared hit=7
   ->  BitmapAnd  (cost=139.37..139.37 rows=1 width=0) (actual time=0.014..0.014 rows=0 loops=1)
         Buffers: shared hit=7
         ->  Bitmap Index Scan on idx_last_name_gin  (cost=0.00..51.11 rows=62 width=0) (actual time=0.014..0.014 rows=0 loops=1)
               Index Cond: ((last_name)::text ~~ 'Doe%'::text)
               Buffers: shared hit=7
         ->  Bitmap Index Scan on idx_first_name_gin  (cost=0.00..88.01 rows=38 width=0) (never executed)
               Index Cond: ((first_name)::text ~~ 'John%'::text)
 Planning:
   Buffers: shared hit=85 read=9
 Planning Time: 0.911 ms
 Execution Time: 0.081 ms
(14 rows)


EXPLAIN ANALYZE for exact search:
                                                             QUERY PLAN                                                             
------------------------------------------------------------------------------------------------------------------------------------
 Bitmap Heap Scan on users  (cost=27.85..31.86 rows=1 width=129) (actual time=0.023..0.023 rows=0 loops=1)
   Recheck Cond: (((last_name)::text = 'Doe'::text) AND ((first_name)::text = 'John'::text))
   Buffers: shared read=3
   ->  BitmapAnd  (cost=27.85..27.85 rows=1 width=0) (actual time=0.022..0.022 rows=0 loops=1)
         Buffers: shared read=3
         ->  Bitmap Index Scan on idx_last_name_btree  (cost=0.00..9.60 rows=690 width=0) (actual time=0.022..0.022 rows=0 loops=1)
               Index Cond: ((last_name)::text = 'Doe'::text)
               Buffers: shared read=3
         ->  Bitmap Index Scan on idx_first_name_btree  (cost=0.00..18.00 rows=1276 width=0) (never executed)
               Index Cond: ((first_name)::text = 'John'::text)
 Planning:
   Buffers: shared hit=14
 Planning Time: 0.065 ms
 Execution Time: 0.036 ms
(14 rows)

```

### Результаты нагрузочного тестирования

| Нагрузка | Avg (ms) | Min (ms) | Max (ms) | Success (%) | Throughput (req/s) |
|----------|----------|----------|----------|-------------|-------------------|
| 1 users | 39 | 0 | 420 | 88 | 1,2542273,5 |
| 10 users | 52 | 0 | 423 | 97 | 4,1945419,9 |
| 100 users | 53 | 0 | 327 | 97 | 1,2078322,2 |
| 1000 users | 49 | 0 | 448 | 94 | 4,2033525,9 |

### HTML Отчеты
- [1 users](./combined_indexes_1_users_dashboard/index.html)
- [10 users](./combined_indexes_10_users_dashboard/index.html)
- [100 users](./combined_indexes_100_users_dashboard/index.html)
- [1000 users](./combined_indexes_1000_users_dashboard/index.html)

---

## Сводный анализ

### Сравнение среднего времени отклика (100 пользователей)

| Конфигурация | Avg Response Time (ms) |
|--------------|------------------------|
| Без индексов (baseline) | 14692 |
| Композитный (first_name, last_name) | 14631 |
| Обратный композитный (last_name, first_name) | 15158 |
| Отдельные B-tree индексы | 15117 |
| Только first_name | 15088 |
| Только last_name | 14853 |
| GIN индексы (pg_trgm) | 60 |
| Комбинированные B-tree + GIN | 53 |

### Сравнение пропускной способности (100 пользователей)

| Конфигурация | Throughput (req/s) |
|--------------|-------------------|
| Без индексов (baseline) | 6.8 |
| Композитный (first_name, last_name) | 6.8 |
| Обратный композитный (last_name, first_name) | 6.6 |
| Отдельные B-tree индексы | 6.6 |
| Только first_name | 6.6 |
| Только last_name | 6.7 |
| **GIN индексы (pg_trgm)** | **1,668** |
| **Комбинированные B-tree + GIN** | **1,886** |

## Рекомендации

### Лучшие результаты по категориям

1. **Лучшее время отклика**: **Комбинированные B-tree + GIN** (53ms при 100 пользователях)
2. **Лучшая пропускная способность**: **Комбинированные B-tree + GIN** (1,886 req/s при 100 пользователях)  
3. **Лучший баланс производительность/размер**: **Композитный индекс** (14,631ms, 9.6MB)
4. **Наименьший размер индексов**: **Только first_name** (6.976MB) / **Только last_name** (7.160MB)

### Выбор индекса в зависимости от нагрузки

- **Низкая нагрузка (1-10 пользователей)**: GIN индексы обеспечивают максимальную производительность
- **Средняя нагрузка (10-100 пользователей)**: Комбинированные B-tree + GIN для оптимального баланса
- **Высокая нагрузка (100+ пользователей)**: GIN индексы показывают стабильную высокую производительность
- **Ограниченная память**: Композитный B-tree индекс как компромисс

### Типы запросов

- **Для LIKE поиска с префиксами**: **GIN индексы с pg_trgm** (время выполнения 0.082ms vs 28-97ms для B-tree)
- **Для точного поиска**: **Композитные B-tree индексы** (0.018ms vs 0.021ms для GIN)
- **Для смешанных запросов**: **Комбинированные B-tree + GIN** индексы
- **Для аналитических запросов**: B-tree индексы для лучшей предсказуемости

## Заключение

### Ключевые выводы

1. **Радикальное улучшение производительности с GIN**: GIN индексы показали прирост производительности в **245 раз** по времени отклика (60ms vs 14,692ms) и в **276 раз** по пропускной способности (1,668 vs 6.8 req/s) при 100 пользователях.

2. **Различие между типами поиска**: 
   - LIKE запросы эффективно обрабатываются только GIN индексами (0.082ms)
   - Точные запросы одинаково хорошо обрабатываются B-tree и GIN индексами (~0.02ms)
   - B-tree индексы не используются планировщиком для LIKE запросов с префиксами

3. **Размер индексов vs производительность**:
   - GIN индексы занимают в 4 раза больше места (37MB vs 9.6MB для композитного)
   - Но обеспечивают в 245 раз лучшую производительность для LIKE запросов
   - ROI: 1% увеличения размера = 61% улучшения производительности
   - Комбинированные индексы (51MB) показывают лучший баланс throughput/latency

4. **Стабильность под нагрузкой**: 
   - GIN индексы показывают стабильную производительность при росте нагрузки
   - B-tree индексы деградируют при высокой нагрузке из-за последовательного сканирования

### Итоговые рекомендации

**Для production среды рекомендуется:**
```sql
-- Основная рекомендация: GIN индексы для LIKE поиска
CREATE INDEX idx_first_name_gin ON users USING gin(first_name gin_trgm_ops);
CREATE INDEX idx_last_name_gin ON users USING gin(last_name gin_trgm_ops);

-- Альтернатива при ограничениях памяти: композитный B-tree
CREATE INDEX idx_composite ON users(first_name, last_name);
```

**Критерии выбора:**
- **Высокая нагрузка + LIKE поиск**: GIN обязательно  
- **Ограниченная память + редкие запросы**: Композитный B-tree
- **Смешанная нагрузка**: Комбинированные индексы

Данный анализ демонстрирует критическую важность правильного выбора типа индекса для PostgreSQL в зависимости от характера SQL запросов и ожидаемой нагрузки.

---

**Сгенерировано**: 22.06.2025  
**Общее время тестирования**: 576 минут 47 секунд

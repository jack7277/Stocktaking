# Stocktaking
Инвентаризация


Загрузка файла базы данных в формате CSV (cp1251) через  FTPS, port 990, парсинг в Realm
Database

Mozilla FTPS Settings in MOZILLA_FTPS_SETTINGS directory

Формат экселя -> сохранить в CSV с разделителем ";", выложить на FTP сервере

Первая строка: Штрихкод (число), Название (текст), Артикул (число),
Количество (целое число), Цена (число с плавающей точкой), Номер коробки
(число)

Если входной файл открыть в notepad++, то там должно быть так:

Штрихкод;Название;Артикул;Количество;Цена;Номер коробки
1001013;Молоко ультрап.Ост 3,2% 0;1001013;0;0.0;0
4600660000000;Молоко ультрап.Ост 3,2% 0;1001013;0;0.0;0

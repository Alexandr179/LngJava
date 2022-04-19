### LngJava

С помощью Java сделать следующее:
а) Считать файл со строчками вида

    A1;B1;C1
    A2;B2;C2
    ...

б) Найти множество уникальных строчек и разбить его на непересекающиеся группы по следующему критерию:

    Если две строчки имеют совпадения непустых значений в одной или более колонках, 
    они принадлежат одной группе. Например, строчки:
    111;123;222
    200;123;100
    300;;100
    все принадлежат одной группе, так как первые две строчки имеют одинаковое значение 123 во второй колонке, 
    а две последние одинаковое значение 100 в третьей колонке

в) Вывести полученные группы в файл в следующем формате:

    Группа 1
        строчка1
        строчка2
        строчка3
        ...
    Группа 2 
        строчка1
        строчка2
        строчка3

    В начале вывода указать получившееся число групп с более чем одним элементом.
    Сверху расположить группы с наибольшим числом элементов.

Архив с данными для выполнения задания lng.7z (https://github.com/PeacockTeam/new-job/blob/master/lng.7z)
Приемлемое время работы на обыкновенном ноутбуке до 30 секунд. 
После выполнения задания необходимо отправить количество полученных групп.

    * Строки вида
     "83000854422549"
     "8383"200000741652251"
     "79855053897"83100000580443402";"200000133000191"
            являются некорректными и должны пропускаться

    ** Если в группе две одинаковых строки 
            - нужно оставить одну
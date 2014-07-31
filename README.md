# Fake Warehouse

this tool can generate fake data with good performance. user needs to define report which includes look up tables and fact table (center table). there should be relationship between these tables, it will be supported in next release. And Project Manager & Launcher **AY** wants to make it easy to use and well defined.
## Report Definition
* `report`
    - storage_path : path to store the report
    - report_name : report name (folder)
    - report
        + LU_table
            look up table such as week, month, year, city, country, etc
        + fact_table : center table, index and metric data
        + LU_fact_relation : relationship columns look up LU_table
* `table`
    - table_name
    - table_rowcount
    - table_slice
        how many slice to store the data. it will slice the data range based on column definition and generate each slice data into slice file.
    - table_cols : column definition
* `column`
    - colname
    - coltype
        it can be inner supported data type (**int, short, boolean, float, double, long, date, string**) or user defined data type which should be extra data which in folder extra such as
        ```
        {
          "extra_config": {
            "month": "./extra/month.json",
            "week": "./extra/week.json"
          }
        }
        ```
        then extra data type month, week can be used as column type
    - colorder
        it can be **desc** (descend), **asc** (ascend), **rand** (random)
    - coldstep
        column step, it's only used for numeric data, it's usually **1**.
    - colrange
        column range, example:
            date range: "2012-06-28~2014-06-15"
            int range: "0~9999999"
for example, you can run tests from code, it contains some basic tests. Or you can refer to tmp/testReportGen.json.

## Third Party Library
* [gson][]
* [common math][]

[gson]: https://code.google.com/p/google-gson/
[common math]: http://commons.apache.org/proper/commons-math/

## About
this tool is lanuched by PM **AY** and me, it's used to generate big data always **billions of rows**. it works well and performance is not so bad.


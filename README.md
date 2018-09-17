# How to build
```gradle shadowJar```

Examples
Go to build\libs
```
java -jar parser-1.0-SNAPSHOT-all.jar --startDate=2017-01-01.00:00:00 --duration=hourly --threshold=200
java -jar parser-1.0-SNAPSHOT-all.jar --accessLog="C:\Users\artem\Downloads\Java_MySQL_Test\access.log" --startDate=2017-01-01.00:00:00 --duration=hourly --threshold=200
```
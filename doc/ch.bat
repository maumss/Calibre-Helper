@echo off

set "JAVA_EXEC=javaw"
set "JAVA_OPTS= -Xms1024m -Xmx1024m"

START "CalibreHelper" /B %JAVA_EXEC% %JAVA_OPTS% -cp "lib\*;" br.com.yahoo.mau_mss.calibrehelper.CalibreHelper >> logs\stdout.log 2>&1

rem %JAVA_EXEC% %JAVA_OPTS% -cp ".\lib\*;" br.com.yahoo.mau_mss.calibrehelper.CalibreHelper

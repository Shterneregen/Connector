set jarName=Connector.jar
echo f | xcopy /y ..\build\libs\%jarName% .\%jarName%
REM pause
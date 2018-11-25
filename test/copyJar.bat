set jarName=Connector.jar
echo f | xcopy /y ..\dist\%jarName% .\%jarName%
REM pause
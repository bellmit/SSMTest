echo ��ʼ������Ŀ...

call %~dp0\compile_develop.bat

cd\
%~d0

cd "%~dp0..\..\.."

start /normal ""  "target"

echo �������! 
@echo off
pause
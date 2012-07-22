@REM
@REM Copyright (c) 2011-2012, Team maxdocs.org
@REM
@REM All rights reserved.
@REM
@REM Redistribution and use in source and binary forms, with or without modification, are permitted provided
@REM that the following conditions are met:
@REM
@REM 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
@REM    following disclaimer.
@REM 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
@REM    the following disclaimer in the documentation and/or other materials provided with the distribution.
@REM 3. The name of the author may not be used to endorse or promote products derived from this software
@REM    without specific prior written permission.
@REM
@REM THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
@REM NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
@REM DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
@REM EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
@REM SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
@REM LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
@REM ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
@REM

@echo off
 @set JVM_OPTS=-XX:MaxPermSize=256m -XX:PermSize=128m -Xms128m -Xmx512m
 (cd .. && java %JVM_OPTS% -jar lib/start-6.1.26.jar)
# Calibre Helper

Swing application to use with `Calibre App`.

## Feature

Creates a html file with many images to `Calibre App` use.

## Install

Create a install with `batch file`:

```shell
@echo off

set "JAVA_EXEC=javaw"
set "JAVA_OPTS= -Xms1024m -Xmx1024m"

START "CalibreHelper" /B %JAVA_EXEC% %JAVA_OPTS% -cp "lib\*;" br.com.yahoo.mau_mss.calibrehelper.CalibreHelper >> logs\stdout.log 2>&1
```

Download `Calibre` [here](https://calibre-ebook.com/) to create your eBook and have fun.

##Usage

Choose de source folder

- Rename chapters
- Don't break page by image

## Credits
Mauricio Soares da Silva - [maumss@users.noreply.github.com](mailto:maumss@users.noreply.github.com)

## License

[GNU General Public License (GPL) v3](http://www.gnu.org/licenses/)

Copyright &copy; 2012 Mauricio Soares da Silva

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.


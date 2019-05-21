W zależności na jakiej wersji Javy pracujemy (domyślnie Java11+) ściągamy Nowik_Java11 i Nowik_Java11_jar lub Nowik_Java09 i Nowik_Java09_jar.
Obie wersje działają tak samo, różnią się trochę kodem.
W folderze Nowik_Java11 i Nowik_Java09 znajdują się kody źródłowe.
W folderze Nowik_Java11_jar i Nowik_Java09_jar znajduje się plik wykonywalny jar.

W przypadku nie posiadania żadnej wersji Javy pobieramy najnowszą wersję ze strony
https://www.oracle.com/technetwork/java/javase/downloads/index.html

Ustawiamy zmienne środowiskowe:

Linux (java-7-openjdk jako przykład):

export JAVA_HOME=/usr/lib/jvm/java-7-openjdk 

export PATH=$PATH:/usr/lib/jvm/java-7-openjdk/bin

Windows:

set path=%path%;C:\Program Files\Java\jdk1.7.0\bin

Aby uruchomić program wpisujemy w konsoli (Windows, Linux):

cd <ścieżka do pliku jar>  np. C:/Users/Nowik/Downloads/Nowik_Java11_jar

java -jar Nowik_Java11.jar

Wykonane w Intellij IDEA Version 2019.1.2
Java JDK 11.0.2, 9.0.4

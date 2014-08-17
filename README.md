ReviewPrepare
=============

Utility for preparing questions for [Code Review][1].

Build
-----

    mvn compile

Run unit tests
--------------

    mvn test

Create jar
----------

    mvn package

This creates two jars in `target/`:

- `reviewprepare.jar`: regular jar
- `reviewprepare-jar-with-dependencies.jar`: executable jar

Test run the executable jar with:

    java -jar target/reviewprepare-jar-with-dependencies.jar src/main/java/net/zomis/reviewprepare/ReviewPreparer.java


[1]: http://codereview.stackexchange.com/

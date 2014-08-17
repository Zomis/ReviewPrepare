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
    # see in target/reviewprepare-1.0-SNAPSHOT.jar

Create executable jar
---------------------

    # executable, with deps
    mvn assembly:single

    # test run it
    java -jar target/reviewprepare-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/java/net/zomis/reviewprepare/ReviewPreparer.java


[1]: http://codereview.stackexchange.com/

# Scala Movie Database

A simple REST API implementation of movie database. It contains a SQLite database of a collection of movies that the user can query from the end points. 

Also includes unit tests.

## Tech Stack

* Scala
* Slick
* akka-http

## How to test
curl -i -X POST localhost:8080/login -d '{"username": "test1", "password":"test1"}' -H "Content-Type: application/json"



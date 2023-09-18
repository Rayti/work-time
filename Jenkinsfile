pipeline {
    agent any
    tools {
        maven 'maven-3.9.2'
    }
    stages {
        stage ('Build') {
            steps {
            sh 'mvn clean package'
            }
        stage ('Tests') {
            steps {
            sh 'mvn test'
            }
        }
    }
}

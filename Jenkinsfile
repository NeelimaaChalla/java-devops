def gv

pipeline {
    agent any
    tools{
        maven 'Maven'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    echo "building jar"
                    sh 'mvn package'
                    //gv.buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image"
                    withCredentials([usernamePassword(credentialsId:'docker-hub-repo',UsernameVariable:'USER',passwordVariable='PSWD')]){
                        sh 'docker build -t neelimachalla/java-devops:jma-2.0'
                        sh "echo $PSWD | docker login -u $USER --password-stdin"
                        sh 'docker push neelimachalla/java-devops:jma-2.0'
                    }
                    //gv.buildImage()
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    echo "deploying"
                    //gv.deployApp()
                }
            }
        }
    }   
}
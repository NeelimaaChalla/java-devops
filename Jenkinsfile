def gv

pipeline {
    agent any
    tools{
        maven 'Maven'
    }
    environment {
        IMAGE_NAME = "neelimachalla/java-devops:3.0"
    }
    stages {
        stage('increment version') {
            steps {
                script {
                    echo 'incrementing app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                         versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    echo "building jar..."
                    sh 'mvn clean package'
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image and pushing to docker repo..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${IMAGE_NAME} ."
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh "docker push ${IMAGE_NAME}"
                    }
                    //gv.buildImage()
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    echo "deploying the application..."
                    def dockercmnd = "docker run -p 8080:8080 -d ${IMAGE_NAME}"
                    sshagent(['ec2-server-key']) {
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@65.2.148.35 ${dockercmnd}"
                    }
                    //gv.deployApp()
                }
            }
        }
        stage('commit version update') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        // git config here for the first time run
                        /*sh 'git config --global user.email "jenkins@example.com"'
                        sh 'git config --global user.name "jenkins"'*/

                        sh 'git status'
                        sh 'git branch'
                        sh 'git config --list'

                        sh "git remote set-url origin https://${USER}:${PASS}@github.com/NeelimaaChalla/java-devops.git"
                        sh 'git add .'
                        sh 'git commit -m "ci: version increment"'
                        sh 'git push origin HEAD:main'
                    }
                }
            }
        }
    }   
}

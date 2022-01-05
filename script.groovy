def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PSWD', usernameVariable: 'USER')]) {
        sh 'docker build -t neelimachalla/java-devops:jma-2.0 .'
        sh "echo $PSWD | docker login -u $USER --password-stdin"
        sh 'docker push neelimachalla/java-devops:jma-2.0'
    }
} 

def deployApp() {
    echo 'deploying the application...'
} 

return this
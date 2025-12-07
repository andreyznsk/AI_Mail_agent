String dockerCred = "DockerCred"



pipeline {
    agent {
        label 'Local'
    }
    options {
        ansiColor('xterm')
    }
    parameters {
        string(
                name: 'REMOTE_HOST',
                defaultValue: '192.168.1.100',
                description: 'IP-адрес или хост для SSH-подключения'
        )
    }
    stages {

        stage('Build') {
            steps {
                script {
                    try {
                        sh "mvn clean package -T 1C -ntp -U"
                    } catch (Exception e) {
                        println e
                    }
                }
            }
        }
        stage('SQ') {
            steps {
                echo 'Send result to SQ'
            }
        }
        stage('Docker build and upload') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: dockerCred, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        // Login to Docker Hub
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"

                        // Build your Docker image
                        sh "docker build -t andreyznsk/app:mailagent.latest -f helm/DockerFile ."

                        // Push the image to Docker Hub
                        sh "docker push andreyznsk/app:mailagent.latest"

                        // Logout from Docker Hub (optional but good practice)
                        sh "docker logout"
                    }
                }
            }
        }

    }
}
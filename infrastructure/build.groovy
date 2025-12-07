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
                name: 'REMOTE_HOSTps ',
                defaultValue: '192.168.1.100',
                description: 'IP-адрес или хост для SSH-подключения'
        )
    }
    stages {

        stage('preTest') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(
                            credentialsId: 'ssh-remote-host', // ID учётных данных в Jenkins
                            keyFileVariable: 'SSH_KEY',
                            usernameVariable: 'SSH_USER'
                    )]) {
                        sh '''
                               echo "Подключаюсь к удалённому хосту: ${REMOTE_HOST}"
                               timeout 30 ssh -o StrictHostKeyChecking=no -i $SSH_KEY $SSH_USER@${REMOTE_HOST}
                               if [ $? -eq 0 ]; then
                                   echo "✅ Подключение успешно"
                               else
                                   echo "❌ Ошибка подключения или команды"
                                   exit 1
                               fi
                           '''
                    }
                }
            }
        }

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
pipeline {
    agent any

    environment {
        IMAGE_NAME = "springbbot-app"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Parallel Tasks') {
            parallel {

                stage('Unit Tests') {
                    steps {
                        sh './mvnw test'
                    }
                }

                stage('Docker Build') {
                    steps {
                        sh 'docker build -t $IMAGE_NAME .'
                    }
                }

                stage('Code Info') {
                    steps {
                        sh 'echo Running additional checks'
                    }
                }
            }
        }

        stage('Run Container (main only)') {
            when {
                branch 'main'
            }
            steps {
                sh '''
                docker stop springbbot-app || true
                docker rm springbbot-app || true
                docker run -d -p 8080:8080 --name springbbot-app springbbot-app
                '''
            }
        }
    }
}

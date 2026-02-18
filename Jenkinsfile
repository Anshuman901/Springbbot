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
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Parallel Tasks') {
            parallel {

                stage('Unit Tests') {
                    steps {
                        bat 'mvnw.cmd test'
                    }
                }

                stage('Docker Build') {
                    steps {
                        bat 'docker build -t %IMAGE_NAME% .'
                    }
                }

                stage('Code Info') {
                    steps {
                        bat 'echo Running additional checks'
                    }
                }
            }
        }

        stage('Run Container (main only)') {
            when {
                branch 'main'
            }
            steps {
                bat '''
                docker stop springbbot-app || exit 0
                docker rm springbbot-app || exit 0
                docker run -d -p 8080:8080 --name springbbot-app springbbot-app
                '''
            }
        }
    }
}


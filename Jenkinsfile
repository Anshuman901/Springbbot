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

        stage('Build Jar (Skip Tests)') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('Run Container (main only)') {
            when {
                branch 'main'
            }
            steps {
                bat '''
                docker stop springbbot-app >nul 2>&1
                docker rm springbbot-app >nul 2>&1
                docker run -d -p 8090:8080 --name springbbot-app springbbot-app
                '''
            }
        }
    }

    post {
        success {
            echo "Build & Deployment Successful"
        }
        failure {
            echo "Build Failed"
        }
    }
}


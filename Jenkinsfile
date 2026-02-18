pipeline {
    agent any

    environment {
        IMAGE_NAME = "springbbot-app"
        IMAGE_TAG = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
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
                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('Deploy - Dev') {
            when {
                branch 'dev'
            }
            steps {
                bat '''
                docker stop springbbot-dev >nul 2>&1
                docker rm springbbot-dev >nul 2>&1
                docker run -d -p 8081:8080 --name springbbot-dev springbbot-app:%IMAGE_TAG%
                '''
            }
        }

        stage('Deploy - Production') {
            when {
                branch 'main'
            }
            steps {
                bat '''
                docker stop springbbot-prod >nul 2>&1
                docker rm springbbot-prod >nul 2>&1
                docker run -d -p 8090:8080 --name springbbot-prod springbbot-app:%IMAGE_TAG%
                '''
            }
        }
    }

    post {
        success {
            echo " Build & Deployment Successful for ${env.BRANCH_NAME}"
        }
        failure {
            echo " Build Failed for ${env.BRANCH_NAME}"
        }
    }
}


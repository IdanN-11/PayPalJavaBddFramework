pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        maven 'maven-3.9'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'Idan',
                    url: 'https://github.com/IdanN-11/PayPalJavaBddFramework.git'
            }
        }

        stage('Clean Workspace') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                bat 'docker-compose up -d'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            echo '📦 Collecting reports...'
            archiveArtifacts artifacts: 'target/**', allowEmptyArchive: true

            echo '🧹 Cleaning Docker containers'
            bat 'docker-compose down'
        }

        failure {
            echo '❌ Tests failed'
        }

        success {
            echo '✅ Tests passed'
        }
    }
}

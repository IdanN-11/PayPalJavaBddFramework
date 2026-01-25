pipeline {
    agent any

    tools {
        jdk 'jdk-17'          // Configure this in Jenkins Global Tools
        maven 'maven-3.9'     // Configure this in Jenkins Global Tools
    }

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
        SELENIUM_GRID_URL = 'http://localhost:4444/wd/hub'
    }

    options {
        timestamps()
        ansiColor('xterm')
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
                sh '''
                  docker compose down -v || true
                  mvn clean || true
                '''
            }
        }

        stage('Start Selenium Grid') {
            steps {
                sh '''
                  docker compose up -d selenium-hub chrome edge1 edge2 edge3 edge4 edge5
                  sleep 15
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                  mvn test \
                    -Dselenium.grid.url=${SELENIUM_GRID_URL}
                '''
            }
        }
    }

    post {

        always {
            echo '📦 Collecting reports...'
            archiveArtifacts artifacts: 'target/**/*.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/**/*.json', allowEmptyArchive: true
        }

        success {
            echo '✅ Tests passed'
        }

        failure {
            echo '❌ Tests failed'
        }

        cleanup {
            echo '🧹 Cleaning Docker containers'
            sh 'docker compose down -v || true'
        }
    }
}

pipeline {
    agent any

    environment {
        // Jenkins Global Tools names
        JAVA_HOME  = tool 'jdk-17'
        MAVEN_HOME = tool 'maven-3.9'

        // Fix PATH for Windows
        PATH = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${env.PATH}"

        // Selenium Grid URL (from docker-compose)
        SELENIUM_GRID_URL = "http://localhost:4444/wd/hub"
    }

    options {
        timestamps()
    }

    stages {

        stage('Verify Tools') {
            steps {
                bat '''
                  echo ===== JAVA =====
                  java -version
                  echo ===== MAVEN =====
                  mvn.cmd -version
                  echo ===== DOCKER =====
                  docker --version
                '''
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'Idan',
                    url: 'https://github.com/IdanN-11/PayPalJavaBddFramework.git'
            }
        }

        stage('Clean Old Containers') {
            steps {
                bat '''
                  docker compose down -v || echo No containers running
                '''
            }
        }

        stage('Build Test Image') {
            steps {
                bat '''
                  docker compose build
                '''
            }
        }

        stage('Start Selenium Grid') {
            steps {
                bat '''
                  docker compose up -d selenium-hub chrome edge1 edge2 edge3
                '''
            }
        }

        stage('Run Tests') {
            steps {
                bat '''
                  mvn.cmd test -Dselenium.grid.url=%SELENIUM_GRID_URL%
                '''
            }
        }
    }

    post {
        always {
            echo "📦 Archiving reports"
            archiveArtifacts artifacts: 'target/**/*.*', allowEmptyArchive: true

            echo "🧹 Stopping containers"
            bat 'docker compose down -v || echo cleanup done'
        }

        success {
            echo "✅ Tests PASSED"
        }

        failure {
            echo "❌ Tests FAILED"
        }
    }
}

pipeline {
    agent any

    environment {
        SELENIUM_GRID_URL = "http://localhost:4444/wd/hub"
    }

    options {
        timestamps()
    }

    stages {

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

        stage('Build Images') {
            steps {
                bat '''
                  docker compose build
                '''
            }
        }

        stage('Run Tests (Docker)') {
            steps {
                bat '''
                  docker compose up --abort-on-container-exit --exit-code-from test-runner
                '''
            }
        }
    }

    post {
        always {
            echo "📦 Archiving reports"
            archiveArtifacts artifacts: 'target/**/*.*', allowEmptyArchive: true

            echo "🧹 Cleaning containers"
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

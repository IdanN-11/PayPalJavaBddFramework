pipeline {
    agent any

    environment {
        SELENIUM_GRID_URL = "http://localhost:4444/wd/hub"
        EMAIL_TO = "idannehra01@gmail.com"
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
        }

        success {
            echo "✅ Tests PASSED"

            emailext(
                subject: "✅ Jenkins SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2 style="color:green;">Automation Tests PASSED</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                    <p>
                      <a href="${env.BUILD_URL}">
                        👉 View Jenkins Build
                      </a>
                    </p>
                """,
                to: "${EMAIL_TO}",
                mimeType: 'text/html'
            )
        }

        failure {
            echo "❌ Tests FAILED"

            emailext(
                subject: "❌ Jenkins FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2 style="color:red;">Automation Tests FAILED</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build:</b> #${env.BUILD_NUMBER}</p>

                    <p>
                      <a href="${env.BUILD_URL}">
                        👉 View Jenkins Logs
                      </a>
                    </p>

                    <p>📎 Reports are attached</p>
                """,
                to: "${EMAIL_TO}",
                mimeType: 'text/html',
                attachmentsPattern: """
                    target/extent-reports/*.html,
                    target/cucumber-reports/*.html
                """
            )
        }

        cleanup {
            echo "🧹 Cleaning containers"
            bat 'docker compose down -v || echo cleanup done'
        }
    }
}

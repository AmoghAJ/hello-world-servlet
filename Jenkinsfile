pipeline {
    agent { label 'ci' }
    options { 
        buildDiscarder(logRotator(numToKeepStr: '5')) 
        }
    environment{
        GIT_SHORT_HASH = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
        ARTIFACT_ZIP   = "hello-world-${GIT_SHORT_HASH}.zip"
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/AmoghAJ/hello-world-servlet.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install -U'
            }
            post {
                success {
                    junit 'target/surefire-reports/*.xml'
                }
                always {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
                failure {
                    sh 'echo "Trigger Slack Notification"'
                }
            }
        }
        stage('Archive artifacts'){
            when {
                branch 'master'
            }
            steps {
                sh """"
                    zip ${ARTIFACT_ZIP} target/*.war
                    aws s3 cp ${ARTIFACT_ZIP} s3://hello-world-ci-artifacts/
                """"
            }
        }  
    }
}
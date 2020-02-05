def getArtifactBucket() {
    String branch = "${env.BRANCH_NAME}"
    String bucketName   =  (branch == 'master') ? "hello-world-ci-artifacts" : "hello-world-ci-artifacts-dev" 
    return bucketName
}   

pipeline {
    agent { label 'ci' }
    options { 
        buildDiscarder(logRotator(numToKeepStr: '5')) 
        }
    environment{
        GIT_SHORT_HASH = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
        ARTIFACT_ZIP   = "hello-world-${GIT_SHORT_HASH}.zip"
        S3_BUCKET      = getArtifactBucket()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -U'
            }
            post {
                success {
                        junit 'target/surefire-reports/*.xml'
                        sh """
                        zip ${ARTIFACT_ZIP} target/*.war
                        aws s3 cp ${ARTIFACT_ZIP} s3://${S3_BUCKET}/
                        """
                        archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                    }
                failure {
                    sh 'echo "Trigger Slack Notification"'
                }
            }
        }  
    }
}
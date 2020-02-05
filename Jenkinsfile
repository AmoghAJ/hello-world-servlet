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
                always {
                    junit 'target/surefire-reports/*.xml'
                }
                success {    
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
        stage('Deploy to QA') {
            when {
                branch 'master'
            }
            steps {
                build job: 'CD', 
                parameters: [string(name: 'APPLICATION', value: 'hello-world'),
                             string(name: 'ENVIORNMENT', value: 'QA')]
            }
            post {
                success {
                    echo 'Promote application artifact to TEST'
                }
            }   
        }
        stage('Deploy to TEST') {
            when {
                branch 'master'
            }
            steps {
                build job: 'CD', 
                parameters: [string(name: 'APPLICATION', value: 'hello-world'),
                             string(name: 'ENVIORNMENT', value: 'TEST'),
                             booleanParam(name: 'RM_APPROVAL', value: true)]
            }
            post {
                success {
                    echo 'Promote application artifact to PROD'
                }
            }
        }
        stage('Deploy to PROD') {
            when {
                branch 'master'
            }
            steps {
                build job: 'CD', 
                parameters: [string(name: 'APPLICATION', value: 'hello-world'),
                             string(name: 'ENVIORNMENT', value: 'PROD'),
                             booleanParam(name: 'RM_APPROVAL', value: true),
                             booleanParam(name: 'MS_APPROVAL', value: true)]
            }
            post {
                success {
                    echo 'Application Deployed to PROD'
                }
            }
        } 
    }
}
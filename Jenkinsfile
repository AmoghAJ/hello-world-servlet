@Library('jenkins-shared-lib')_

def releaseConfig = [
        version: misc.getReleaseVersion(),
        deploy: [            
            qa: true,
            test: true,
            prod: true
        ]
]

ci config: releaseConfig
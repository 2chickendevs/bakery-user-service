resource "aws_cloudwatch_log_group" "app_logs" {
  name              = "/ec2/docker-app"
  retention_in_days = 7
}
output "alb_dns_name" {
  value = aws_lb.load_balancer.dns_name
}

output "cloudwatch_log_group" {
  value = aws_cloudwatch_log_group.app_logs.name
}
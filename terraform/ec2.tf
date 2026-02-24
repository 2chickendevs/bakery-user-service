resource "aws_instance" "app" {
  ami           = var.ami_id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.instance.id]
  iam_instance_profile   = aws_iam_instance_profile.ec2_profile.name

  user_data = templatefile("${path.module}/user_data.sh.tftpl", {
    docker_image = var.docker_image
    app_port     = var.app_port
    region       = "us-east-1"
    env_all      = local.env_all
    log_group    = aws_cloudwatch_log_group.app_logs.name
  })

  tags = {
    Name = "web-app-1"
  }
}
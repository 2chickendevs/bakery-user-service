############################
# Security Groups
############################
resource "aws_security_group" "alb" {
  name        = "alb-security-group"
  description = "ALB SG"
  vpc_id      = data.aws_vpc.default_vpc.id
}

resource "aws_security_group_rule" "alb_http_in" {
  type              = "ingress"
  security_group_id = aws_security_group.alb.id
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "alb_all_out" {
  type              = "egress"
  security_group_id = aws_security_group.alb.id
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group" "instance" {
  name        = "instance-security-group"
  description = "EC2 instance SG"
  vpc_id      = data.aws_vpc.default_vpc.id
}

# Allow app traffic ONLY from the ALB to the instance
resource "aws_security_group_rule" "app_in_from_alb" {
  type                     = "ingress"
  security_group_id        = aws_security_group.instance.id
  from_port                = var.app_port
  to_port                  = var.app_port
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.alb.id
}

# Instance needs outbound internet to pull Docker image
resource "aws_security_group_rule" "instance_all_out" {
  type              = "egress"
  security_group_id = aws_security_group.instance.id
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
}
namespace Subscriber
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.Subscribe1Btn = new System.Windows.Forms.Button();
            this.Unsubscribe1Btn = new System.Windows.Forms.Button();
            this.Subscribe2Btn = new System.Windows.Forms.Button();
            this.Unsubscribe2Btn = new System.Windows.Forms.Button();
            this.Subscribe3Btn = new System.Windows.Forms.Button();
            this.Unsubscribe3Btn = new System.Windows.Forms.Button();
            this.Received1TextBox = new System.Windows.Forms.TextBox();
            this.Received2TextBox = new System.Windows.Forms.TextBox();
            this.Received3TextBox = new System.Windows.Forms.TextBox();
            this.SuspendLayout();
            // 
            // Subscribe1Btn
            // 
            this.Subscribe1Btn.Location = new System.Drawing.Point(12, 12);
            this.Subscribe1Btn.Name = "Subscribe1Btn";
            this.Subscribe1Btn.Size = new System.Drawing.Size(95, 23);
            this.Subscribe1Btn.TabIndex = 0;
            this.Subscribe1Btn.Text = "Subscribe Msg1";
            this.Subscribe1Btn.UseVisualStyleBackColor = true;
            this.Subscribe1Btn.Click += new System.EventHandler(this.Subscribe1Btn_Click);
            // 
            // Unsubscribe1Btn
            // 
            this.Unsubscribe1Btn.Location = new System.Drawing.Point(113, 12);
            this.Unsubscribe1Btn.Name = "Unsubscribe1Btn";
            this.Unsubscribe1Btn.Size = new System.Drawing.Size(110, 23);
            this.Unsubscribe1Btn.TabIndex = 1;
            this.Unsubscribe1Btn.Text = "Unsubscribe Msg 1";
            this.Unsubscribe1Btn.UseVisualStyleBackColor = true;
            this.Unsubscribe1Btn.Click += new System.EventHandler(this.Unsubscribe1Btn_Click);
            // 
            // Subscribe2Btn
            // 
            this.Subscribe2Btn.Location = new System.Drawing.Point(12, 41);
            this.Subscribe2Btn.Name = "Subscribe2Btn";
            this.Subscribe2Btn.Size = new System.Drawing.Size(95, 23);
            this.Subscribe2Btn.TabIndex = 2;
            this.Subscribe2Btn.Text = "Subscribe Msg 2";
            this.Subscribe2Btn.UseVisualStyleBackColor = true;
            this.Subscribe2Btn.Click += new System.EventHandler(this.Subscribe2Btn_Click);
            // 
            // Unsubscribe2Btn
            // 
            this.Unsubscribe2Btn.Location = new System.Drawing.Point(113, 41);
            this.Unsubscribe2Btn.Name = "Unsubscribe2Btn";
            this.Unsubscribe2Btn.Size = new System.Drawing.Size(110, 23);
            this.Unsubscribe2Btn.TabIndex = 3;
            this.Unsubscribe2Btn.Text = "Unsubscribe Msg 2";
            this.Unsubscribe2Btn.UseVisualStyleBackColor = true;
            this.Unsubscribe2Btn.Click += new System.EventHandler(this.Unsubscribe2Btn_Click);
            // 
            // Subscribe3Btn
            // 
            this.Subscribe3Btn.Location = new System.Drawing.Point(12, 70);
            this.Subscribe3Btn.Name = "Subscribe3Btn";
            this.Subscribe3Btn.Size = new System.Drawing.Size(95, 23);
            this.Subscribe3Btn.TabIndex = 4;
            this.Subscribe3Btn.Text = "Subscribe Msg 3";
            this.Subscribe3Btn.UseVisualStyleBackColor = true;
            this.Subscribe3Btn.Click += new System.EventHandler(this.Subscribe3Btn_Click);
            // 
            // Unsubscribe3Btn
            // 
            this.Unsubscribe3Btn.Location = new System.Drawing.Point(113, 70);
            this.Unsubscribe3Btn.Name = "Unsubscribe3Btn";
            this.Unsubscribe3Btn.Size = new System.Drawing.Size(110, 23);
            this.Unsubscribe3Btn.TabIndex = 5;
            this.Unsubscribe3Btn.Text = "Unsubscribe Msg 3";
            this.Unsubscribe3Btn.UseVisualStyleBackColor = true;
            this.Unsubscribe3Btn.Click += new System.EventHandler(this.Unsubscribe3Btn_Click);
            // 
            // Received1TextBox
            // 
            this.Received1TextBox.Location = new System.Drawing.Point(229, 14);
            this.Received1TextBox.Name = "Received1TextBox";
            this.Received1TextBox.ReadOnly = true;
            this.Received1TextBox.Size = new System.Drawing.Size(151, 20);
            this.Received1TextBox.TabIndex = 6;
            // 
            // Received2TextBox
            // 
            this.Received2TextBox.Location = new System.Drawing.Point(229, 43);
            this.Received2TextBox.Name = "Received2TextBox";
            this.Received2TextBox.ReadOnly = true;
            this.Received2TextBox.Size = new System.Drawing.Size(151, 20);
            this.Received2TextBox.TabIndex = 7;
            // 
            // Received3TextBox
            // 
            this.Received3TextBox.Location = new System.Drawing.Point(229, 72);
            this.Received3TextBox.Name = "Received3TextBox";
            this.Received3TextBox.ReadOnly = true;
            this.Received3TextBox.Size = new System.Drawing.Size(151, 20);
            this.Received3TextBox.TabIndex = 8;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(397, 134);
            this.Controls.Add(this.Received3TextBox);
            this.Controls.Add(this.Received2TextBox);
            this.Controls.Add(this.Received1TextBox);
            this.Controls.Add(this.Unsubscribe3Btn);
            this.Controls.Add(this.Subscribe3Btn);
            this.Controls.Add(this.Unsubscribe2Btn);
            this.Controls.Add(this.Subscribe2Btn);
            this.Controls.Add(this.Unsubscribe1Btn);
            this.Controls.Add(this.Subscribe1Btn);
            this.Name = "Form1";
            this.Text = "Subscriber";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button Subscribe1Btn;
        private System.Windows.Forms.Button Unsubscribe1Btn;
        private System.Windows.Forms.Button Subscribe2Btn;
        private System.Windows.Forms.Button Unsubscribe2Btn;
        private System.Windows.Forms.Button Subscribe3Btn;
        private System.Windows.Forms.Button Unsubscribe3Btn;
        private System.Windows.Forms.TextBox Received1TextBox;
        private System.Windows.Forms.TextBox Received2TextBox;
        private System.Windows.Forms.TextBox Received3TextBox;
    }
}


namespace Publisher
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
            this.Notify1Btn = new System.Windows.Forms.Button();
            this.Notify2Btn = new System.Windows.Forms.Button();
            this.Notify3Btn = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // Notify1Btn
            // 
            this.Notify1Btn.Location = new System.Drawing.Point(12, 12);
            this.Notify1Btn.Name = "Notify1Btn";
            this.Notify1Btn.Size = new System.Drawing.Size(75, 23);
            this.Notify1Btn.TabIndex = 0;
            this.Notify1Btn.Text = "Notify 1";
            this.Notify1Btn.UseVisualStyleBackColor = true;
            this.Notify1Btn.Click += new System.EventHandler(this.Notify1Btn_Click);
            // 
            // Notify2Btn
            // 
            this.Notify2Btn.Location = new System.Drawing.Point(12, 41);
            this.Notify2Btn.Name = "Notify2Btn";
            this.Notify2Btn.Size = new System.Drawing.Size(75, 23);
            this.Notify2Btn.TabIndex = 1;
            this.Notify2Btn.Text = "Notify 2";
            this.Notify2Btn.UseVisualStyleBackColor = true;
            this.Notify2Btn.Click += new System.EventHandler(this.Notify2Btn_Click);
            // 
            // Notify3Btn
            // 
            this.Notify3Btn.Location = new System.Drawing.Point(12, 70);
            this.Notify3Btn.Name = "Notify3Btn";
            this.Notify3Btn.Size = new System.Drawing.Size(75, 23);
            this.Notify3Btn.TabIndex = 2;
            this.Notify3Btn.Text = "Notify 3";
            this.Notify3Btn.UseVisualStyleBackColor = true;
            this.Notify3Btn.Click += new System.EventHandler(this.Notify3Btn_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(220, 148);
            this.Controls.Add(this.Notify3Btn);
            this.Controls.Add(this.Notify2Btn);
            this.Controls.Add(this.Notify1Btn);
            this.Name = "Form1";
            this.Text = "Publisher";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button Notify1Btn;
        private System.Windows.Forms.Button Notify2Btn;
        private System.Windows.Forms.Button Notify3Btn;
    }
}

